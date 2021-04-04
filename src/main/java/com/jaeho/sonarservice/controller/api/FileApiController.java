package com.jaeho.sonarservice.controller.api;

import com.jaeho.sonarservice.domain.model.FileDto;
import com.jaeho.sonarservice.service.FileService;
import com.jaeho.sonarservice.service.SonarqubeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class FileApiController {

    private FileService fileService;

    private SonarqubeService sonarqubeService;

    /**
     * 세션에 있는 유저정보를 통해 유저가 upload한 파일목록을 보여주는 메서드
     * @param httpSession 유저정보를 얻기위한 session
     * @return
     */
    @GetMapping("/files")
    public ResponseEntity list(HttpSession httpSession) {
        List<FileDto> responseData = fileService.list(httpSession);
        return new ResponseEntity<List<FileDto>>(responseData, HttpStatus.OK);
    }

    @DeleteMapping("/files")
    public ResponseEntity delete(@RequestParam int fileId) {
        fileService.deleteById(fileId);
        sonarqubeService.deleteSonarqubeProjectByFileId(fileId);
        return ResponseEntity.ok().build();
    }
}
