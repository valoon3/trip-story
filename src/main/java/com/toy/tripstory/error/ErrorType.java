package com.toy.tripstory.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    // 사실상 정의되지 못한 에러
    SERVER_ERROR("001", "서버 에러입니다..", HttpStatus.INTERNAL_SERVER_ERROR),

    NOT_EXISTS_AUTHORIZATION("A-003", "Authorization Header가 빈값입니다.", HttpStatus.UNAUTHORIZED),
    NOT_VALID_BEARER_GRANT_TYPE("A-004", "인증 타입이 Bearer 타입이 아닙니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("A-001", "토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    NOT_VALID_TOKEN("A-002", "해당 토큰은 유효한 토큰이 아닙니다.", HttpStatus.UNAUTHORIZED),

    MEMBER_NOT_EXISTS("M-003", "해당 회원은 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    NOT_SAME_PASSWORD("M-004", "비밀번호가 같지 않습니다. 비밀번호를 확인해주세요", HttpStatus.BAD_REQUEST),
    NOT_VALID_PASSWORD("AUTH-001", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),


    ;

    // 에러 코드 (예: "A-001", "M-002" 등)
    private String errorCode;

    // 에러 메시지 (예: "토큰이 만료되었습니다.", "이미 가입된 회원입니다." 등)
    private String errorMessage;

    private HttpStatus httpStatus;

    /**
     * ErrorType 생성자입니다.
     *
     * @param errorCode 에러 코드
     * @param errorMessage 에러 메시지
     */
    ErrorType(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    ErrorType(String errorCode, String errorMessage, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
