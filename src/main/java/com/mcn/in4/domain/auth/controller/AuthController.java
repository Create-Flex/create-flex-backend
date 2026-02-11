package com.mcn.in4.domain.auth.controller;

import com.mcn.in4.domain.auth.controller.api.AuthApi;
import com.mcn.in4.domain.auth.dto.request.AuthRequestDTO;
import com.mcn.in4.domain.auth.dto.response.AuthResponseDTO;
import com.mcn.in4.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 인증 컨트롤러
 * - 로그인, 로그아웃, 토큰 재발급 API 제공
 * - JWT Access Token과 Refresh Token 기반 인증 처리
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthService authService;

    /**
     * 로그인 API
     * - 사번(memberAccount)과 비밀번호로 인증
     * - 성공 시 Access Token(30분)과 Refresh Token(7일) 반환
     * @param request 로그인 요청 DTO (memberAccount, password)
     * @return Access Token, Refresh Token 포함 응답
     */
    @Override
    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO.loginRequestDto request) {
        return authService.login(request);
    }

    /**
     * 로그아웃 API
     * - Redis에서 Refresh Token 삭제
     * - SecurityContext 인증 정보 제거
     * - 이후 해당 Refresh Token으로 재발급 불가
     * @return 로그아웃 완료 메시지
     */
    @Override
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // SecurityContext에서 현재 인증된 사용자의 memberId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            authService.logout(authentication.getName());
        }
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    /**
     * 토큰 재발급 API
     * - Access Token 만료 시 Refresh Token으로 새 토큰 발급
     * - Refresh Token Rotation: 새 Refresh Token도 함께 발급
     * - Redis에 저장된 Refresh Token과 비교하여 유효성 검증
     * @param request refreshToken을 담은 Map
     * @return 새로운 Access Token, Refresh Token 포함 응답
     */
    @Override
    @PostMapping("/reissue")
    public AuthResponseDTO reissue(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        return authService.reissue(refreshToken);
    }
}
