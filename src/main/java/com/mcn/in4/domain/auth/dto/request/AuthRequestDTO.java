package com.mcn.in4.domain.auth.dto.request;

import lombok.*;

public class AuthRequestDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class loginRequestDto {
//        @NotBlank(message = "아이디는 필수입니다")
        private String memberAccount;
//        @NotBlank(message = "비밀번호는 필수입니다")
        private String password;
    }

}
