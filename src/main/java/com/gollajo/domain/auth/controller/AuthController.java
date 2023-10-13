package com.gollajo.domain.auth.controller;

import com.gollajo.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/kakao/callback")
    public ResponseEntity<Void> loginByKakao(@RequestParam("code") String code) {

        authService.register(code);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
