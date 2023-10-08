package com.gollajo.domain.test.controller;

import com.gollajo.domain.test.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    private final TestService testService;

    @Operation(summary = "testAPI", description = "API를 테스트한다.")
    @ApiResponse(responseCode = "200", description = "테스트 성공")
    @GetMapping
    public ResponseEntity<String> test(){
        final String testMessage = testService.test();
        return ResponseEntity.ok(testMessage);
    }

}
