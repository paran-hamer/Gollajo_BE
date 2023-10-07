package com.gollajo.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public enum ErrorCode {
    NO_USERNAME(HttpStatus.BAD_REQUEST, 201, "닉네임은 필수입니다.");



    ErrorCode(HttpStatus statusCode,int customCode,String message){
        this.statusCode = statusCode;
        this.customCode = customCode;
        this.message = message;
    }

    private final HttpStatus statusCode;
    private final int customCode;
    private final String message;
}
