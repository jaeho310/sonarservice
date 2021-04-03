package com.jaeho.sonarservice.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice {

    /**
     * 파일업로드 에러 핸들링
     * @param e Exception e
     * @return
     */
    @ExceptionHandler(FileException.class)
    public ModelAndView businessError(Exception e) {
        log.error(e.toString());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/files/upload");
        mav.addObject("message", e.getMessage());
        return mav;
    }

}
