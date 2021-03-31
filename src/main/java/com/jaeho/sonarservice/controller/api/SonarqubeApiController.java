package com.jaeho.sonarservice.controller.api;

import com.jaeho.sonarservice.domain.model.SonarqubeMeasure;
import com.jaeho.sonarservice.service.SonarqubeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sonar")
@AllArgsConstructor
public class SonarqubeApiController {

    SonarqubeService sonarqubeService;

    /**
     * 파일정보를 통해 maven build, 소나큐브 분석을 하는 메서드
     * @param fileId
     * @return
     * @throws Throwable
     */
    @PostMapping("/analysis")
    public ResponseEntity analysis(@RequestParam int fileId, HttpSession httpSession) throws Throwable {
        boolean analysis = sonarqubeService.analysis(fileId, httpSession);
        return ResponseEntity.ok().build();
    }

    /**
     * 소나큐브 api를 통해 데이터를 받아오는 메서드
     * @param sonarqubeId
     * @return
     */
    @PostMapping("/measure")
    public ResponseEntity measure(@RequestParam int sonarqubeId) {
        SonarqubeMeasure response = sonarqubeService.measure(sonarqubeId);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * 유저정보를 통해 소나큐브 분석한 list를 반환하는 메서드
     * @param httpSession
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity list(HttpSession httpSession) {
        List<Map<String, Object>> response = sonarqubeService.getSonarqubeList(httpSession);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * 소나큐브 서버 url을 받아오는 메서드
     * @return
     */
    @GetMapping("/url")
    public ResponseEntity getSonarUrl() {
        Map<String, String> sonarUrl = sonarqubeService.getSonarUrl();
        return new ResponseEntity(sonarUrl, HttpStatus.OK);
    }
}
