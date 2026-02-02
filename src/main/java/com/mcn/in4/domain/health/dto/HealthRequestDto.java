package com.mcn.in4.domain.health.dto;

import com.mcn.in4.domain.health.entity.CheckupSummanary;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class HealthRequestDto {

    //Post, 파일을 업로드하기위한 요청을 받기 위한 Request
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class HealthUpload{
        private Long memberId;
        private String name;
        private LocalDate date;
        private CheckupSummanary summanary;
        private MultipartFile file;
    }

    //Get, 일정목록을 반환받기위한 요청을 받기 위한 Request
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class HealthList{
        private Long memberId;
        private LocalDate searchStartDate;
        private LocalDate searchEndDate;
    }
}
