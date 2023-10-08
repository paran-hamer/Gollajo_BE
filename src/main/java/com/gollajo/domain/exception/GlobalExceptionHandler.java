package com.gollajo.domain.exception;

import com.gollajo.domain.exception.dto.ErrorCodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorCodeResponse> handleCustomException(CustomException e){
        ErrorCode errorCode = e.getErrorCode();
        ErrorCodeResponse body = ErrorCodeResponse.builder()
                .customCode(errorCode.getCustomCode())
                .message(errorCode.getMessage())
                .build();
        return new ResponseEntity<>(body,errorCode.getStatusCode());
    }
}
