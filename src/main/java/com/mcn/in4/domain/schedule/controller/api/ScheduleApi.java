package com.mcn.in4.domain.schedule.controller.api;

import com.mcn.in4.domain.schedule.dto.responseDTO.SchedulReponseDTO;
import com.mcn.in4.domain.schedule.dto.resquestDTO.ScheduleRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@Tag(name = "일정관리", description = "일정 관리 API")
public interface ScheduleApi {
    @Operation(
            summary = "나의 일정 등록",
            description = "현재 로그인한 사용자의 일정을 등록합니다. 일반 직원은 개인 일정으로, 매니저는 크리에이터 일정을 등록할 때 사용합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "일정 등록 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터")
            }
    )
    @PostMapping("/")
    ResponseEntity<String> createSchedule(
            @Parameter(hidden = true) String userId,
            @Parameter(hidden = true) Authentication authentication,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "등록할 일정 데이터",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n  \"scheduleName\": \"주간 업무 보고서 작성\",\n  \"scheduleDate\": \"2026-02-04\",\n  \"scheduleDetail\": \"2월 1주차 업무 실적 정리 및 2월 계획 수립\",\n  \"scheduleType\": \"PERSONAL\"\n}"
                            )
                    )
            )
            @RequestBody ScheduleRequestDTO.ScheduleCreateRequestDto requestDto
    );

    @Operation(
            summary = "나의 월간 일정 조회",
            description = "특정 월을 기준으로 본인이 관련된 모든 일정을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공")
            }
    )
    @GetMapping("/me")
    ResponseEntity<List<SchedulReponseDTO.ScheduleResponseDto>> getMyMonthlySchedules(
            @Parameter(hidden = true) String userId,
            @Parameter(description = "조회 년월 (YYYY-MM)", example = "2026-02")
            @RequestParam("month") String month
    );
}
