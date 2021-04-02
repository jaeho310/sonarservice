package com.jaeho.sonarservice.domain.dao;

import com.jaeho.sonarservice.domain.model.FileDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface FileDao {
    int insertFile(FileDto fileDto);

    List<FileDto> getFiles(int user_Uid);

    FileDto getByFileId(int fileId);

    int isExist(String fileId);

    int deleteById(int fileId);

    int checkProjectNameDuplicated(String projectName, int userId);
}
