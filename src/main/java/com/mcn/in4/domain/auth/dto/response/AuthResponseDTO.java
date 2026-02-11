package com.mcn.in4.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 인증 응답 DTO
 * - 로그인, 토큰 재발급 시 클라이언트에 반환
 * - Access Token과 Refresh Token을 함께 전달
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDTO {
    /**
     * Access Token (JWT)
     * - API 요청 시 Authorization 헤더에 포함
     * - 만료 시간: 30분
     * - 클레임: memberId, role, type("access")
     */
    private String accessToken;

    /**
     * Refresh Token (JWT)
     * - Access Token 만료 시 재발급용
     * - 만료 시간: 7일
     * - Redis에 저장되어 서버에서 관리
     */
    private String refreshToken;
}
