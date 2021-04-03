package com.jaeho.sonarservice.controller.api;

import com.jaeho.sonarservice.domain.model.UserDto;
import com.jaeho.sonarservice.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserApiController {

    private UserService userService;

    /**
     * 회원가입 메서드
     * @param userDto 저장할 멤버정보
     * @return ResponseEntity를 통해 Header와 Body 관련 정보를 return 해준다.
     */
    @PostMapping("/join")
    public ResponseEntity joinMember(@RequestBody UserDto userDto) {
        userService.insertMember(userDto);
        return new ResponseEntity("가입되었습니다.", HttpStatus.OK);
    }

    /**
     * ID 중복체크 메서드
     * @param userId 유저 아이디
     * @return true는 중복, false는 중복되지 않은 ID
     */
    @GetMapping("/{userId}/exist")
    public boolean checkIdDuplicate(@PathVariable String userId) {
        return userService.checkIdDuplicate(userId);
    }

    /**
     * 로그인 메서드
     * @param httpSession 세션
     * @param userLoginRequest 입력한 아이디와 비밀번호
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public ResponseEntity userLogin(HttpSession httpSession, @RequestBody UserLoginRequest userLoginRequest) throws Exception {
        UserDto userDto = userService.userLogin(userLoginRequest.getUserId(), userLoginRequest.getPassword(), httpSession);
        return new ResponseEntity<UserDto> (userDto,HttpStatus.OK);
    }

    /**
     * 로그아웃 메서드
     * @param httpSession invalidate를 위한 세션
     * @return
     */
    @GetMapping("/logout")
    public ResponseEntity userLogout(HttpSession httpSession) {
        httpSession.invalidate();
        return ResponseEntity.ok().build();
    }


    @Getter
    private static class UserLoginRequest {
        @NonNull String userId;
        @NonNull String password;
    }
}
