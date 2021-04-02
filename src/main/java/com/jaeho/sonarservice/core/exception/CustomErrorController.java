package com.jaeho.sonarservice.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class CustomErrorController implements ErrorController {
    private static final String path = "/error";

    @Override
    public String getErrorPath() { return path; }

    @RequestMapping(value = path)
    public String Error(HttpServletRequest req) {

        String status = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) + "";
        log.info("Error!! info:" + status);

        switch(status) {
            case "403" : return "error/error403";
            case "500" : return "error/error500";
            default : return "error/error404";
        }
    }

}
