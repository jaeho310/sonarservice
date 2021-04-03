package com.jaeho.sonarservice.service;

import com.jaeho.sonarservice.domain.model.UserDto;
import com.jaeho.sonarservice.domain.dao.UserDao;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@AllArgsConstructor
@Service
public class UserService {

    private UserDao userDao;

    private PasswordEncoder passwordEncoder;

    /**
     * 회원가입 메서드
     * @param userDto 유저정보를 담는 객체
     */
    @Transactional
    public void insertMember(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        int insertCnt = userDao.insertMember(userDto);
        if (insertCnt != 1) {
            throw new RuntimeException("필수정보를 모두 입력해주세요.");
        }
    }

    public boolean checkIdDuplicate(String memberId) {
        return userDao.checkIdDuplicate(memberId) == 1;
    }

    /**
     * db에 복호화된 password와 유저가 입력한 password를 복호화한 값이 같은지 비교
     * 로그인 성공시 20분의 유지시간을 가진 세션 부여
     * @param userId 사용자가 입력한 아이디
     * @param password 사용자가 입력한 비밀번호
     */
    public UserDto userLogin(String userId, String password, HttpSession httpSession) {
        String encodedPassword = userDao.getEncodedPassword(userId);
        boolean matches = passwordEncoder.matches(password, encodedPassword);
        if (!matches) {
            throw new RuntimeException("등록되지 않은 유저입니다.");
        }
        UserDto userDto = userDao.userLogin(userId);
        httpSession.setAttribute("UserInfo", userDto);
        httpSession.setMaxInactiveInterval(20*60);
        return userDto;
    }
}
