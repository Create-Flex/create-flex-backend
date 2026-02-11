package com.mcn.in4.domain.auth.service;

import com.mcn.in4.domain.auth.dto.request.AuthRequestDTO;
import com.mcn.in4.domain.auth.dto.response.AuthResponseDTO;

/**
 * 인증 서비스 인터페이스
 * - 로그인, 로그아웃, 토큰 재발급 비즈니스 로직 정의
 * - JWT + Redis 기반 인증 처리
 */
public interface AuthService {

    /**
     * 로그인
     * @param request 로그인 요청 (사번, 비밀번호)
     * @return Access Token, Refresh Token
     */
    AuthResponseDTO login(AuthRequestDTO.loginRequestDto request);

    /**
     * 로그아웃
     * - Redis에서 Refresh Token 삭제
     * @param memberId 로그아웃할 회원 ID
     */
    void logout(String memberId);

    /**
     * 토큰 재발급
     * - Refresh Token 검증 후 새 토큰 쌍 발급
     * - Refresh Token Rotation 적용
     * @param refreshToken 클라이언트의 Refresh Token
     * @return 새로운 Access Token, Refresh Token
     */
    AuthResponseDTO reissue(String refreshToken);
}
