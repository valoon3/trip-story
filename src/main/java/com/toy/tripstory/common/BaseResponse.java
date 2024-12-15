package com.toy.tripstory.common;

import com.toy.tripstory.common.dto.ResponseBodyDto;
import com.toy.tripstory.error.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseResponse<T> extends ResponseEntity<ResponseBodyDto<T>> {

    public BaseResponse(T data, HttpStatus status) {
        super(new ResponseBodyDto<>(data), status);
    }

    public BaseResponse(ErrorType errorType, String cause, String detailMessage) {
        super(new ResponseBodyDto<>(
                null,
                errorType.getErrorCode() + " : " + cause,
                        detailMessage
                ),
                errorType.getHttpStatus()
        );
    }

    public BaseResponse(ErrorType errorType) {
        super(new ResponseBodyDto<>(
                        null,
                        errorType.getErrorCode(),
                        errorType.getErrorMessage()
                ),
                errorType.getHttpStatus()
        );
    }

    // 필요에 따라 상태 추가
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(data, HttpStatus.OK);
    }

    public static <T> BaseResponse<T> fail(ErrorType errorType) {
        return new BaseResponse<>(errorType);
    }

    public static <T> BaseResponse<T> fail(ErrorType errorType, String cause, String detailMessage) {
        return new BaseResponse<>(errorType, cause, detailMessage);
    }
}
