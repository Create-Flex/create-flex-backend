package com.mcn.in4.domain.auth.controller;

import com.mcn.in4.domain.auth.controller.api.AuthApi;
import com.mcn.in4.domain.auth.dto.request.AuthRequestDTO;
import com.mcn.in4.domain.auth.dto.response.AuthResponseDTO;
import com.mcn.in4.domain.auth.service.AuthService;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import com.mcn.in4.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 컨트롤러
 * - 로그인, 로그아웃, 토큰 재발급 API 제공
 * - Access Token: Response Body로 반환
 * - Refresh Token: HttpOnly Cookie로 설정 (XSS 공격 방어)
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthService authService;

    /** 쿠키 Secure 옵션 (개발: false, 운영: true) */
    @Value("${app.cookie.secure:false}")
    private boolean cookieSecure;

    /**
     * 로그인 API
     * - 사번(memberAccount)과 비밀번호로 인증
     * - Access Token은 Response Body로 반환
     * - Refresh Token은 HttpOnly Cookie로 설정
     * @param request 로그인 요청 DTO (memberAccount, password)
     * @param response HttpServletResponse (쿠키 설정용)
     * @return Access Token 포함 응답
     */
    @Override
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody AuthRequestDTO.loginRequestDto request,
            HttpServletResponse response) {

        AuthResponseDTO authResponse = authService.login(request);

        // Refresh Token을 HttpOnly 쿠키로 설정
        ResponseCookie cookie = CookieUtil.createRefreshTokenCookie(authResponse.getRefreshToken(), cookieSecure);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // Response Body에는 Access Token만 반환
        AuthResponseDTO responseBody = AuthResponseDTO.builder()
                .accessToken(authResponse.getAccessToken())
                .build();

        return ResponseEntity.ok(responseBody);
    }

    /**
     * 로그아웃 API
     * - Redis에서 Refresh Token 삭제
     * - Refresh Token 쿠키 제거
     * - SecurityContext 인증 정보 제거
     * @param response HttpServletResponse (쿠키 삭제용)
     * @return 로그아웃 완료 메시지
     */
    @Override
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // SecurityContext에서 현재 인증된 사용자의 memberId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            authService.logout(authentication.getName());
        }

        // Refresh Token 쿠키 삭제
        ResponseCookie deleteCookie = CookieUtil.deleteRefreshTokenCookie(cookieSecure);
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    /**
     * 토큰 재발급 API
     * - HttpOnly 쿠키에서 Refresh Token 추출
     * - 새 Access Token 발급 및 Refresh Token Rotation
     * @param request HttpServletRequest (쿠키에서 Refresh Token 추출)
     * @param response HttpServletResponse (새 쿠키 설정용)
     * @return 새로운 Access Token 포함 응답
     */
    @Override
    @PostMapping("/reissue")
    public ResponseEntity<AuthResponseDTO> reissue(
            HttpServletRequest request,
            HttpServletResponse response) {

        // 쿠키에서 Refresh Token 추출
        String refreshToken = CookieUtil.getRefreshTokenFromCookie(request)
                .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_INVALID, "Refresh Token이 없습니다."));

        AuthResponseDTO authResponse = authService.reissue(refreshToken);

        // 새 Refresh Token을 HttpOnly 쿠키로 설정 (Rotation)
        ResponseCookie cookie = CookieUtil.createRefreshTokenCookie(authResponse.getRefreshToken(), cookieSecure);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // Response Body에는 Access Token만 반환
        AuthResponseDTO responseBody = AuthResponseDTO.builder()
                .accessToken(authResponse.getAccessToken())
                .build();

        return ResponseEntity.ok(responseBody);
    }
}
