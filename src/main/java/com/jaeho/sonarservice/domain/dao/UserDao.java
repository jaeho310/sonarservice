package com.jaeho.sonarservice.domain.dao;

import com.jaeho.sonarservice.domain.model.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao {
    int insertMember(UserDto userDto);

    int checkIdDuplicate(String memberId);

    UserDto userLogin(String userId);

    String getEncodedPassword(String userId);
}
