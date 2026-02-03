package com.mcn.in4.global.error.exception;


import org.springframework.security.core.AuthenticationException;
import lombok.Getter;
@Getter
public class CustomAuthenticationException extends AuthenticationException {
    private final ErrorCode errorCode;
    public CustomAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}