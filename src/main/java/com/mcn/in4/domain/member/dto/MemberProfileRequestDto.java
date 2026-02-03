package com.mcn.in4.domain.member.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

public class MemberProfileRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProfileUpload{
        private MultipartFile file;
    }

}
