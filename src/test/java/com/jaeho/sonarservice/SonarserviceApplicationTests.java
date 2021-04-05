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
}
