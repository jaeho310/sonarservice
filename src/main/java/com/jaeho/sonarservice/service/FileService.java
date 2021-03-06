package com.jaeho.sonarservice.service;

import com.jaeho.sonarservice.core.exception.FileException;
import com.jaeho.sonarservice.domain.dao.FileDao;
import com.jaeho.sonarservice.domain.model.FileDto;
import com.jaeho.sonarservice.domain.model.UserDto;
import lombok.extern.slf4j.Slf4j;
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

    public FileService(FileDao fileDao, SonarqubeService sonarqubeService) {
        this.fileDao = fileDao;
    }

    @Value("${file.location}")
    private String rootLocation;

    /**
     * inputStream을 가져와서 저장위치로 파일을 쓰며 기존에 존재하면 대체하는 형식
     * @param multipartFile 업로드한 파일
     * @param httpSession 유저정보를 얻기위한 session
     */
    public void fileUpload(MultipartFile multipartFile, String projectName, HttpSession httpSession) {
        UserDto userInfo = (UserDto) httpSession.getAttribute("UserInfo");
        String fileName = multipartFile.getOriginalFilename();
        String fileExtension = this.getFileExtension(fileName);

        if(!fileExtension.equals(".zip")) {
            throw new FileException("zip파일을 업로드 해주세요");
        }

        boolean isDuplicatedProjectName = this.checkProjectNameDuplicated(projectName, userInfo.getId());
        if (isDuplicatedProjectName) {
            throw new FileException("중복된 프로젝트명입니다. 프로젝트명을 변경해주세요");
        }

        boolean isDuplicatedFileName = this.checkFileNameDuplicated(fileName,userInfo.getId());
        if (isDuplicatedFileName) {
            throw new FileException("중복된 파일입니다. 목록을 확인해주세요");
        }
        String downloadDir = rootLocation;
        downloadDir = downloadDir + "/" + userInfo.getUserId();
        File makeFolder = new File(downloadDir);
        if(!makeFolder.exists()) {
            makeFolder.mkdir();
            log.info("{} 폴더생성", downloadDir);
        }
        log.info(downloadDir);
        Path copyOfLocation = Paths.get(downloadDir + File.separator +StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        try {
            Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FileException("파일업로드 실패");
        }
        FileDto fileDto = FileDto.builder().userId(userInfo.getId()).fileName(multipartFile.getOriginalFilename()).projectName(projectName).build();
        this.insertFile(fileDto);
    }

    private boolean checkFileNameDuplicated(String fileName, int userId) {
        int result = fileDao.checkFileNameDuplicated(fileName, userId);
        return result != 0;
    }

    /**
     * 파일확장자를 얻기 위한 메서드
     * @param originalFilename 확장자가 포함된 파일이름
     * @return
     */
    private String getFileExtension(String originalFilename) {
        return originalFilename.substring(originalFilename.length() - 4);
    }

    /**
     * 동일한 프로젝트명으로 파일업로드를 방지하기위한 메서드
     * @param projectName 사용자가 입력한 프로젝트이름
     * @return
     */
    private boolean checkProjectNameDuplicated(String projectName, int userId) {
        int result = fileDao.checkProjectNameDuplicated(projectName, userId);
        return result != 0;
    }

    /**
     * 파일저장후 해당파일정보를 table에 insert 하는 메서드
     * @param fileDto 파일정보를 담은 객체
     * @return
     */
    @Transactional
    public int insertFile(FileDto fileDto) {
        return fileDao.insertFile(fileDto);
    }

    /**
     * 유저정보를 통해 유저가 저장한 file list를 리턴
     * @param httpSession 유저정보를 얻기위한 session
     * @return
     */
    public List<FileDto> list(HttpSession httpSession) {
        UserDto userInfo = (UserDto) httpSession.getAttribute("UserInfo");
        return fileDao.getFiles(userInfo.getId());
    }

    /**
     * 유저아이디와 FileID를 이용해 유저가 저장한 파일정보를 리턴하는 메서드
     * @param fileId fileUid
     */
    public FileDto getByFileId(int fileId) {
        return fileDao.getByFileId(fileId);
    }

    /**
     * 파일목록에서 파일을 제거하는 메서드
     * @param fileId fileUid
     */
    @Transactional
    public void deleteById(int fileId) {
        int cnt = fileDao.deleteById(fileId);
        if (cnt != 1) {
            throw new FileException("파일 삭제 실패");
        }
    }
}
