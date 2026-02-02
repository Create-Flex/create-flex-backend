package com.mcn.in4.global.error;

import com.mcn.in4.global.error.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private int status;
    private String message;
    private ErrorResponse(final ErrorCode code) {
        this.status = code.getStatus().value();
        this.message = code.getMessage();
    }
    private ErrorResponse(final ErrorCode code, final String message) {
        this.status = code.getStatus().value();
        this.message = message;
    }
    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }
    public static ErrorResponse of(final ErrorCode code, final String message) {
        return new ErrorResponse(code, message);
    }
}