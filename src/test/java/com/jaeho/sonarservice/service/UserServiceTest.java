package com.jaeho.sonarservice.service;

import com.jaeho.sonarservice.domain.model.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    @DisplayName("회원가입")
    void test() {
        UserDto user = UserDto.builder().name("최재호").userId("jaeho310").password("123").build();
        userService.insertMember(user);
    }

    @Test
    @DisplayName("로그인")
    void test2() {
//        UserDto userDto = userService.userLogin("jaeho310", "123");
//        System.out.println("userDto = " + userDto);
    }

}