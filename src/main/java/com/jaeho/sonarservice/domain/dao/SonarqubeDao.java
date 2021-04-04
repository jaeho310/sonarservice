package com.jaeho.sonarservice.domain.dao;

import com.jaeho.sonarservice.domain.model.SonarqubeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SonarqubeDao {

    int insertSonarqube(SonarqubeDto sonarqubeDto);

    int getCnt(int fileId);

    List<Map<String,Object>> getByUserUid(int userUid);

    SonarqubeDto getById(int sonarqubeId);

    int deleteByFileId(int fileId);

    SonarqubeDto getByFileId(int fileId);
}
