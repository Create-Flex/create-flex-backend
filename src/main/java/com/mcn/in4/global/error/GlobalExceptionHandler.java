package com.mcn.in4.global.error;

import com.mcn.in4.global.error.exception.CustomAuthenticationException;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 로직 예외 처리
     */
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(final CustomException e) {
        log.error("handleCustomException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode, e.getDetailedMessage());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     * 인증 관련 예외 처리 (Security)
     */
    @ExceptionHandler(CustomAuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handleAuthenticationException(final CustomAuthenticationException e) {
        log.error("handleAuthenticationException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     * Bean Validation 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * SSE 타임아웃 예외 처리
     */
    @ExceptionHandler(AsyncRequestTimeoutException.class)
    protected ResponseEntity<String> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException e) {
        // 타임아웃은 SSE의 정상적인 연결 종료 과정이므로 INFO 레벨로 간결하게 기록
        log.info("SSE Connection timeout occurred (Expected behavior)");
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    /**
     * 그 외 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception e, HttpServletRequest request) {
        // SSE 요청인지 확인 (Accept 헤더 또는 subscribe 경로)
        String acceptHeader = request.getHeader("Accept");
        boolean isSseRequest = (acceptHeader != null && acceptHeader.contains("text/event-stream")) ||
                request.getRequestURI().contains("/subscribe");

        if (isSseRequest) {
            if (e instanceof IOException && (e.getMessage() != null &&
                    (e.getMessage().contains("Broken pipe") || e.getMessage().contains("connection response")
                            || e.getMessage().contains("software caused connection abort")))) {
                log.info("SSE client disconnected: {}", e.getMessage());
            } else {
                log.warn("SSE exception occurred: {}", e.getMessage());
            }
            // SSE는 JSON 에러 응답을 보낼 수 없으므로 상태 코드만 반환
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }

        log.error("handleException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        return new ResponseEntity<>(response, ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }
}