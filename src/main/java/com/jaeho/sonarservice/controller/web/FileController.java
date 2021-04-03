package com.jaeho.sonarservice.controller.web;

import com.jaeho.sonarservice.domain.model.FileDto;
import com.jaeho.sonarservice.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.ConditionalOnRepositoryType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @GetMapping("/upload")
    public String list(){
        return "files/upload";
    }

    /**
     *
     * @param file 사용자가 업로드한 파일
     * @param projectName 파일에 대한 프로젝트 이름
     * @param httpSession 사용자 정보를 얻기위한 session
     * @return
     */
    @PostMapping("/upload")
    public ModelAndView fileUpload(@RequestParam("file") MultipartFile file, @RequestParam("projectName") String projectName, HttpSession httpSession) {
        fileService.fileUpload(file, projectName, httpSession);
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", "파일업로드 성공");
        mav.setViewName("files/upload");
        return mav;
//        return "redirect:/files/upload";
    }

}
