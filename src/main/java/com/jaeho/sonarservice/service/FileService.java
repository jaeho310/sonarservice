package com.jaeho.sonarservice.service;

import com.jaeho.sonarservice.core.exception.BusinessException;
import com.jaeho.sonarservice.domain.dao.FileDao;
import com.jaeho.sonarservice.domain.dao.SonarqubeDao;
import com.jaeho.sonarservice.domain.model.FileDto;
import com.jaeho.sonarservice.domain.model.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@Slf4j
public class FileService {

    private FileDao fileDao;

    private SonarqubeDao sonarqubeDao;

    public FileService(FileDao fileDao, SonarqubeDao sonarqubeDao) {
        this.fileDao = fileDao;
        this.sonarqubeDao = sonarqubeDao;
    }

    @Value("${file.location}")
    private String rootLocation;

    /**
     * inputStream을 가져와서 저장위치로 파일을 쓰며 기존에 존재하면 대체하는 형식
     * @param multipartFile 업로드한 파일
     * @param httpSession 유저정보를 얻기위한 session
     */
    public void fileUpload(MultipartFile multipartFile, String projectName, HttpSession httpSession) {
        String downloadDir = rootLocation;
        UserDto userInfo = (UserDto) httpSession.getAttribute("UserInfo");
        downloadDir = downloadDir + "/" + userInfo.getUserId();
        File makeFolder = new File(downloadDir);
        if(!makeFolder.exists()) {
            makeFolder.mkdir();
            log.info("{} 폴더생성", downloadDir);
        }
        Path copyOfLocation = Paths.get(downloadDir + File.separator +StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        try {
            Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BusinessException("파일업로드 실패");
        }
        FileDto fileDto = FileDto.builder().userId(userInfo.getId()).fileName(multipartFile.getOriginalFilename()).projectName(projectName).build();
        this.insertFile(fileDto);
    }

    /**
     * 파일저장후 해당파일정보를 table에 insert 하는 메서드
     * @param fileDto
     * @return
     */

    @Transactional
    public int insertFile(FileDto fileDto) {
        return fileDao.insertFile(fileDto);
    }

    /**
     * 유저정보를 통해 유저가 저장한 file list를 리턴
     * @param httpSession
     * @return
     */
    public List<FileDto> list(HttpSession httpSession) {
        UserDto userInfo = (UserDto) httpSession.getAttribute("UserInfo");
        return fileDao.getFiles(userInfo.getId());
    }

    /**
     * 유저아이디와 FileIdID를 이용해 유저가 저장한 파일정보를 리턴하는 메서드
     * @param fileId
     */
    public FileDto getByFileId(int fileId) {
        return fileDao.getByFileId(fileId);
    }

    @Transactional
    public void deleteById(int fileId) {
        int cnt = fileDao.deleteById(fileId);
        if (cnt != 1) {
            throw new BusinessException("파일 삭제 실패");
        }
        cnt = sonarqubeDao.deleteByFileId(fileId);
    }
}
