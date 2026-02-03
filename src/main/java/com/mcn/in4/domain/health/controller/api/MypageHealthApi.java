package com.mcn.in4.domain.health.controller.api;

import com.mcn.in4.domain.health.dto.HealthRequestDto;
import com.mcn.in4.domain.health.dto.HealthResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Health")
public interface MypageHealthApi {

    @Operation(
            summary = "회원 건강 정보 기간 조회",
            description = "memberId와 시작/종료 날짜를 기준으로 해당 기간의 건강 정보를 조회합니다."
    )
    @GetMapping("/")
    List<HealthResponseDto.HealthInfo> generateHealthInfo(

            @AuthenticationPrincipal String userId,

            @Parameter(
                    description = "조회 시작 날짜",
                    example = "2024-11-15",
                    required = true
            )
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @Parameter(
                    description = "조회 종료 날짜",
                    example = "2025-11-15",
                    required = true
            )
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    );

    @Operation(
            summary = "건강 정보 업로드 Presigned URL 생성",
            description = "건강 검진 파일 업로드를 위한 Presigned URL을 생성합니다."
    )
    @PostMapping("/upload")
    @ResponseBody
    HealthResponseDto.HealthPresigned generatePresignedUrl(

            @AuthenticationPrincipal String userId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "건강 정보 업로드 요청 데이터",
                    required = true
            )
            HealthRequestDto.HealthUpload request
    );


}
