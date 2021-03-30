package com.jaeho.sonarservice.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sonar")
public class SonarqubeController {

    @GetMapping("/analysis")
    public String analysis() {
        return "sonarqube/analysis";
    }

    @GetMapping("/dashboard")
    public String dashBoard() {
        return "sonarqube/dashboard";
    }
}
