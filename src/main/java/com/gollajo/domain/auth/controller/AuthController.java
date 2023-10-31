package com.gollajo.domain.auth.controller;

import com.gollajo.domain.auth.dto.LoginResponse;
import com.gollajo.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<LoginResponse> loginByKakao(@RequestParam("code") String code, HttpServletRequest request) {
        log.info("여기까지 진입");
        HttpSession session = request.getSession(true);


        Long registerMemberId = authService.register(code);

        session.setAttribute("memberId",registerMemberId);
        session.setMaxInactiveInterval(1800);

        return new ResponseEntity<>(new LoginResponse("성공"),HttpStatus.OK);
    }

}
