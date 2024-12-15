package com.toy.tripstory.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBodyDto<T> {
    private T data;
    private boolean success;
    private String message;
    private String logMessage;

    public ResponseBodyDto(T data) {
        this.data = data;
        this.success = true;
    }

    public ResponseBodyDto(T data, String message) {
        this(data);
        this.success = false;
        this.message = message;
    }

    public ResponseBodyDto(T data, String message, String logMessage) {
        this(data, message);
        this.logMessage = logMessage;
    }
}
