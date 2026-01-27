package com.mcn.in4.domain.auth.service;

import com.mcn.in4.domain.auth.dto.request.AuthRequestDTO;
import com.mcn.in4.domain.auth.dto.response.AuthResponseDTO;
import com.mcn.in4.domain.auth.repoitory.AuthRepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.repository.MemberRepository;
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
    @Override
    public AuthResponseDTO login(AuthRequestDTO.loginRequestDto request) {
        Member member = memberRepository.findByMemberAccount(request.getMemberAccount())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 해시 비밀번호 확인
//        if (!passwordEncoder.matches(request.getPassword(), member.getMemberPassword())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
        // 비밀번호 확인
        if (!(request.getPassword().equals(member.getMemberPassword()))) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        // 토큰 생성 (Member ID를 String으로 변환하여 전달)
        String token = jwtTokenProvider.generateToken(String.valueOf(member.getMemberId()));
        //  DTO 반환
        return AuthResponseDTO.builder()
                .accesstoken(token)
                .build();
    }
    @Override
    public void logout() {
        // 현재 인증 정보 제거
        SecurityContextHolder.clearContext();
    }

}
