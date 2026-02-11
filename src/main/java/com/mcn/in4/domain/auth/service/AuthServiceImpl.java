package com.mcn.in4.domain.auth.service;

import com.mcn.in4.domain.auth.dto.request.AuthRequestDTO;
import com.mcn.in4.domain.auth.dto.response.AuthResponseDTO;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.repository.MemberRepository;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import com.mcn.in4.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthResponseDTO login(AuthRequestDTO.loginRequestDto request) {
        Member member = memberRepository.findByMemberAccount(request.getMemberAccount())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 해시 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), member.getMemberPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String memberId = String.valueOf(member.getMemberId());
        String role = member.getMemberRole().name();

        // Access Token 생성
        String accessToken = jwtTokenProvider.generateAccessToken(memberId, role);

        // Refresh Token 생성 및 Redis 저장
        String refreshToken = jwtTokenProvider.generateRefreshToken(memberId);
        refreshTokenService.saveRefreshToken(memberId, refreshToken);

        // DTO 반환
        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String memberId) {
        // Redis에서 Refresh Token 삭제
        refreshTokenService.deleteRefreshToken(memberId);
        // 현재 인증 정보 제거
        SecurityContextHolder.clearContext();
    }

    @Override
    public AuthResponseDTO reissue(String refreshToken) {
        log.info("========== 토큰 갱신 요청 시작 ==========");

        // Refresh Token 유효성 검증
        if (jwtTokenProvider.validateToken(refreshToken).isEmpty()) {
            log.warn("유효하지 않은 Refresh Token");
            throw new CustomException(ErrorCode.TOKEN_INVALID, "유효하지 않은 Refresh Token입니다.");
        }

        // 토큰에서 memberId 추출
        String memberId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        log.info("토큰 갱신 요청 - memberId: {}", memberId);

        // Redis에 저장된 Refresh Token과 비교
        if (!refreshTokenService.validateRefreshToken(memberId, refreshToken)) {
            log.warn("Redis의 Refresh Token과 불일치 - memberId: {}", memberId);
            throw new CustomException(ErrorCode.TOKEN_INVALID, "Refresh Token이 일치하지 않습니다.");
        }

        // Member 조회하여 role 가져오기
        Member member = memberRepository.findById(Long.parseLong(memberId))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND, "회원을 찾을 수 없습니다."));

        String role = member.getMemberRole().name();

        // 새 Access Token 생성
        String newAccessToken = jwtTokenProvider.generateAccessToken(memberId, role);

        // 새 Refresh Token 생성 및 Redis 갱신 (Rotation)
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(memberId);
        refreshTokenService.saveRefreshToken(memberId, newRefreshToken);

        log.info("토큰 갱신 완료 - memberId: {}, role: {}", memberId, role);
        log.info("========== 토큰 갱신 요청 끝 ==========");

        return AuthResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
