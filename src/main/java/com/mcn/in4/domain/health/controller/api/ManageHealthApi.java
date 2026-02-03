package com.mcn.in4.domain.health.controller.api;

import com.mcn.in4.domain.health.dto.HealthResponseDto.HealthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Health")
public interface ManageHealthApi {
    @Operation(
            summary = "관리자용 건강 정보 전체 조회",
            description = "관리자가 확인하는 전체 건강 정보 목록을 반환합니다."
    )
    @GetMapping("/")
    List<HealthInfo> generateManageHealthInfo();

    @Operation(
            summary = "건강 정보 검색",
            description = """
        이름(포함 검색), 기간 조건으로 건강 정보를 조회합니다.
        - 이름만 가능
        - 기간만 가능
        - 이름 + 기간 가능
        - 아무 조건 없으면 전체 조회
        """
    )
    @GetMapping("/search")
    List<HealthInfo> search(
            @Parameter(
                    description = "회원 이름 (포함 검색)",
                    example = "김인",
                    required = false
            )
            @RequestParam(required = false) String name,

            @Parameter(
                    description = "검색 시작 날짜",
                    example = "2024-10-10",
                    required = false
            )
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @Parameter(
                    description = "검색 종료 날짜",
                    example = "2024-11-28",
                    required = false
            )
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    );
}
