package com.jaeho.sonarservice.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public ModelAndView businessError(Exception e) {
        log.error(e.toString());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/error/businesserror");
        mav.addObject("message", e.getMessage());
        return mav;
    }

}
