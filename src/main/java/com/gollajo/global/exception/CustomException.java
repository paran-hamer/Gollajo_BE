package com.gollajo.global.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CustomException extends RuntimeException{

    ErrorCode errorCode;

    @Builder
    public CustomException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }


}
