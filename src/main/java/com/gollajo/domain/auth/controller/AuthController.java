package com.gollajo.domain.auth.controller;

import com.gollajo.domain.auth.dto.LoginResponse;
import com.gollajo.domain.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<LoginResponse> loginByKakao(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) {

        //TODO: 처음화면에서 쿠키값가 세션확인하는 API를 만들고 만약 이미 쿠키에 값이 있고 이것이 올바른 값이라면 이 로그인창말고 로그인처리,
        //TODO: 만약 없다면 이 창을 통해 회원이라면 로그인시키고(세션에 저장,쿠키값 넘김), 회원이 아니라면 회원가입후 로그인(세션에 저장,쿠키값넘김)
        //TODO: 로그아웃시 세션에 있던 내용을 삭제시켜주기
        Long registerMemberId = authService.register(code);

        ResponseCookie cookie = ResponseCookie.from("memberId", String.valueOf(registerMemberId))
                .maxAge(60 * 60 * 1)
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .build();
        response.addHeader(SET_COOKIE,cookie.toString());


        return new ResponseEntity<>(new LoginResponse("성공"),HttpStatus.OK);
    }

}
