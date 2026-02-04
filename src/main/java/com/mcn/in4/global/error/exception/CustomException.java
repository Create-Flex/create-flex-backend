package com.mcn.in4.global.error.exception;

import lombok.Getter;
@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String detailedMessage;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailedMessage = errorCode.getMessage();
    }

    public CustomException(ErrorCode errorCode, String detailedMessage) {
        super(detailedMessage);
        this.errorCode = errorCode;
        this.detailedMessage = detailedMessage;
    }
}