package com.toy.tripstory.error.exception;

import com.project.springboard.error.ErrorType;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{

    private ErrorType errorType;

    public BadRequestException(ErrorType errorType) {
        // RuntimeException의 생성자에 에러 메시지를 전달하여 초기화
        super(errorType.getErrorMessage());
        this.errorType = errorType;
    }

}
