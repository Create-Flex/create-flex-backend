package com.mcn.in4.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

import java.util.Arrays;
import java.util.Optional;

/**
 * 쿠키 유틸리티 클래스
 * - HttpOnly 쿠키 생성 및 조회를 위한 헬퍼 메서드 제공
 * - Refresh Token을 안전하게 저장하기 위해 사용
 */
public class CookieUtil {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private static final long REFRESH_TOKEN_MAX_AGE = 7 * 24 * 60 * 60; // 7일 (초 단위)

    /**
     * Refresh Token용 HttpOnly 쿠키 생성
     * @param refreshToken Refresh Token 값
     * @param secure HTTPS 전용 여부 (개발: false, 운영: true)
     * @return ResponseCookie 객체 (Set-Cookie 헤더에 사용)
     */
    public static ResponseCookie createRefreshTokenCookie(String refreshToken, boolean secure) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .httpOnly(true)           // JavaScript 접근 차단 (XSS 방어)
                .secure(secure)           // HTTPS 전용 여부
                .sameSite("Lax")          // 개발 환경 호환성을 위해 Lax 사용
                .path("/api/auth")        // 인증 관련 경로에서만 전송
                .maxAge(REFRESH_TOKEN_MAX_AGE)
                .build();
    }

    /**
     * Refresh Token 쿠키 삭제 (로그아웃 시 사용)
     * @param secure HTTPS 전용 여부
     * @return maxAge=0인 ResponseCookie (브라우저에서 쿠키 삭제)
     */
    public static ResponseCookie deleteRefreshTokenCookie(boolean secure) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(secure)
                .sameSite("Lax")
                .path("/api/auth")
                .maxAge(0)  // 즉시 만료
                .build();
    }

    /**
     * 요청에서 Refresh Token 쿠키 값 추출
     * @param request HttpServletRequest
     * @return Refresh Token 값 (Optional)
     */
    public static Optional<String> getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
