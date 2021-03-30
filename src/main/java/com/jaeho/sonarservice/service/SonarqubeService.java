package com.jaeho.sonarservice.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jaeho.sonarservice.domain.dao.SonarqubeDao;
import com.jaeho.sonarservice.domain.model.FileDto;
import com.jaeho.sonarservice.domain.model.SonarqubeDto;
import com.jaeho.sonarservice.domain.model.SonarqubeMeasure;
import com.jaeho.sonarservice.domain.model.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class SonarqubeService {

    private UnzipService unzipService;

    private FileService fileService;

    private SonarqubeDao sonarqubeDao;

    private final String rootDir = "./maven_files/";

    /**
     * DB에서 파일정보를 select, 소나큐브 분석
     * @param fileId
     * @return
     * @throws Throwable
     */
    public boolean analysis(int fileId, HttpSession httpSession) throws Throwable {
        FileDto fileDto = fileService.getByFileId(fileId);
        String pureFileName = fileDto.getFileName().replace(".zip","");

        UserDto userInfo = (UserDto) httpSession.getAttribute("UserInfo");
        unzipService.decompress(fileDto.getFileName(), userInfo.getUserId());
        mavenInstall(pureFileName,userInfo.getUserId());

        // 분석후, 소나큐브에 테이블에 isnert
        // 이전에 insert 한경우 skip
        if (sonarqubeDao.getCnt(fileDto.getId()) == 0) {
            SonarqubeDto sonarqubeDto = SonarqubeDto
                    .builder()
                    .sonarqubeName(pureFileName)
                    .sonarqubeKey(pureFileName + ".key")
                    .mavenFileId(fileDto.getId())
                    .build();
            sonarqubeDao.insertSonarqube(sonarqubeDto);
        }

        return true;
    }

    /**
     * mvn 명령어를 통해 sonarqube 분석
     * 서버PC에 소나큐브서버를 띄워놔야 가능하다.
     * @param pureFileName
     * @throws IOException
     * @throws InterruptedException
     */
    private void mavenInstall(String pureFileName, String userId) throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        command.add("cmd");
        command.add("/c");
        command.add("mvn clean install -f " + rootDir + "/" + userId + "/" + pureFileName +
                " sonar:sonar -Dsonar.login=736739cce07c73eddba140fdee00b5363fa64e22" +
                " -Dsonar.projectKey=" + pureFileName + ".key"+
                " -Dsonar.host.url=http://127.0.0.1:9000 -Dsonar.projectName=" + pureFileName);
        for (String comm : command) {
            log.info("{}",comm);
        }
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        StringBuilder stringBuilder = new StringBuilder();
        if (process != null) {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
                System.out.println("line = " + line);
            }
        }

        process.waitFor();
    }

    /**
     * 소나큐브 API를 사용해 대시보드에 필요한 데이터를 return해준다.
     * @param sonarqubeId
     * @return sonarqubeMeasure
     */
    public SonarqubeMeasure measure(int sonarqubeId) {
        SonarqubeDto sonarqubeDto = sonarqubeDao.getById(sonarqubeId);
        String sonarqubeKey = sonarqubeDto.getSonarqubeKey();
        String hostUrl = "http://127.0.0.1:9000";
        String apiUrl = "/api/measures/component";
        String metricKeys = "bugs,vulnerabilities,code_smells,coverage,ncloc,duplicated_lines_density";

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(hostUrl+apiUrl)
                .queryParam("component",sonarqubeKey)
                .queryParam("metricKeys",metricKeys)
                .build();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor("admin","admin!@34"));

        ResponseEntity<String> exchange = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity<String>(headers), String.class);
        String body = exchange.getBody();
        JsonParser jsonParser = new JsonParser();
        JsonArray JsonArray = jsonParser.parse(body)
                .getAsJsonObject().get("component")
                .getAsJsonObject().get("measures")
                .getAsJsonArray();

        SonarqubeMeasure sonarqubeMeasure = new SonarqubeMeasure();
        for (JsonElement jsonElement : JsonArray) {
            String metric = jsonElement.getAsJsonObject().get("metric").getAsString();
            String value = jsonElement.getAsJsonObject().get("value").getAsString();
            switch (metric) {
                case "bugs":
                    sonarqubeMeasure.setBugs(Integer.parseInt(value));
                    break;
                case "duplicated_lines_density":
                    sonarqubeMeasure.setDuplicated(Double.parseDouble(value));
                    break;
                case "code_smells":
                    sonarqubeMeasure.setCodeSmell(Integer.parseInt(value));
                    break;
                case "ncloc":
                    sonarqubeMeasure.setLoc(Integer.parseInt(value));
                    break;
                case "coverage":
                    sonarqubeMeasure.setCoverage(Double.parseDouble(value));
                    break;
                case "vulnerabilities":
                    sonarqubeMeasure.setVulnerability(Integer.parseInt(value));
                    break;
                default:
                    break;
            }
        }
        log.info("{}",sonarqubeMeasure);
        return sonarqubeMeasure;
    }

    /**
     * 사용자에게 정적분석을 완료한 프로젝트 명을 보여주기 위한 메서드
     * @param httpSession
     * @return
     */
    public List<Map<String, Object>> getSonarqubeList(HttpSession httpSession) {
        UserDto userInfo = (UserDto) httpSession.getAttribute("UserInfo");
        int userUid = userInfo.getId();
        return sonarqubeDao.getByUserUid(userUid);
    }
}
