package com.jaeho.sonarservice;

import com.google.gson.*;
import com.jaeho.sonarservice.domain.model.SonarqubeMeasure;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;

@Slf4j
class SonarserviceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void test() {
		String url1 = "http://127.0.0.1:9000";
		String url2 = "/api/measures/component";
		String metricKeys = "bugs,vulnerabilities,code_smells,coverage,ncloc,duplicated_lines_density";

		UriComponents builder = UriComponentsBuilder.fromHttpUrl(url1+url2)
				.queryParam("component","com.example:jacocotest")
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
		System.out.println("sonarqubeMeasure = " + sonarqubeMeasure);
	}


//	@Test
//	void postTest() {
//		RestTemplate restTemplate = new RestTemplate();
//		restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor("admin", "admin"));
//
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
////        params.add("key", "AXRXWNJDre8wox3yxxPD");
//		params.add("key", "AXelLOEkgy7ezNVuY-EG");
//		params.add("rule", "common-java:InsufficientLineCoverage");
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("headerTest", "headerValue");
//
//		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//
//		ResponseEntity<String> response = restTemplate.postForEntity("http://203.235.210.119:9000/api/qualityprofiles/activate_rule", request , String.class);
//		log.info("{}",response.getStatusCode());
//		log.info("{}",response.getStatusCodeValue());
//
//	}
//
//	@Test
//	void searchTest() {
//		RestTemplate restTemplate = new RestTemplate();
//		restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor("admin", "admin"));
//
//		ResponseEntity<String> response = restTemplate.getForEntity("http://203.235.210.119:9000/api/qualityprofiles/search", String.class);
//		log.info("{}",response.getStatusCodeValue());
//
//		// 이미 스트링..
////        Gson gson = new Gson();
////        String json = gson.toJson(response.getBody());
//
//
//		JsonParser parser = new JsonParser();
//		JsonElement element = parser.parse(response.getBody());
//		JsonObject jsonObject = element.getAsJsonObject();
//		JsonArray profiles = jsonObject.get("profiles").getAsJsonArray();
//		for (JsonElement profile : profiles) {
////            System.out.println("profile = " + profile);
//			String language = profile.getAsJsonObject().get("language").getAsString();
//			if (language.equals("java")) {
//				System.out.println("key = " + profile.getAsJsonObject().get("key"));
//				System.out.println("name = " + profile.getAsJsonObject().get("name"));
//			}
//		}
//	}
//
//	@Test
//	@DisplayName("소나큐브 add_project api 테스트용")
//	void addProjectTest() {
//		RestTemplate restTemplate = new RestTemplate();
//		restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor("admin", "admin"));
//
//		HttpHeaders headers = new HttpHeaders();
//		LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//		params.add("language","java");
//		params.add("project", "pims.tms");
//		params.add("qualityProfile","sq_rule_test_java");
//
//		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//
//		ResponseEntity<String> response = restTemplate.postForEntity("http://203.235.210.119:9000/api/qualityprofiles/add_project", request , String.class);
//
//	}

}
