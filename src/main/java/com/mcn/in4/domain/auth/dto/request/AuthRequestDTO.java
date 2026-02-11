package com.mcn.in4.domain.auth.dto.request;

import lombok.*;

/**
 * 인증 요청 DTO 클래스
 * - 로그인 등 인증 관련 요청 데이터를 담는 DTO
 */
public class AuthRequestDTO {

    /**
     * 로그인 요청 DTO
     * - 사번(memberAccount)과 비밀번호(password)를 담아 전송
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class loginRequestDto {
        /** 사번 (로그인 아이디) - 예: "HR001" */
        private String memberAccount;

        /** 비밀번호 (평문, 서버에서 BCrypt로 검증) */
        private String password;
    }

}
