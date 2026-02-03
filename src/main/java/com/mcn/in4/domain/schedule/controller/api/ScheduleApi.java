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
            description = "일정의 성격(합방, 개인, 대행 등)에 따라 적절한 파라미터를 전송합니다.",
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
                    description = "상황별 일정 등록 예시",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "1. 합방(MERGE) 일정 등록",
                                            summary = "매니저가 크리에이터 합방 일정을 등록할 때",
                                            value = "{\n  \"scheduleName\": \"감스트 x 또간집 콜라보 먹방\",\n  \"scheduleDate\": \"2026-02-15\",\n  \"scheduleDetail\": \"강남역 인근 맛집 탐방 및 합방 진행\",\n  \"scheduleType\": \"MERGE\",\n  \"creatorId\": 2001,\n  \"visitorIds\": [2002, 2005]\n}"
                                    ),
                                    @ExampleObject(
                                            name = "2. 매니저의 크리에이터 일정 대행 등록",
                                            summary = "매니저가 담당 크리에이터의 단독 일정을 등록할 때",
                                            value = "{\n  \"scheduleName\": \"갤럭시 S26 홍보 영상 촬영\",\n  \"scheduleDate\": \"2026-02-20\",\n  \"scheduleDetail\": \"삼성전자 본사 스튜디오 촬영\",\n  \"scheduleType\": \"PROMOTION\",\n  \"creatorId\": 2001,\n  \"visitorIds\": []\n}"
                                    ),
                                    @ExampleObject(
                                            name = "3. 크리에이터 본인 일정 등록",
                                            summary = "크리에이터가 직접 자신의 일정을 등록할 때",
                                            value = "{\n  \"scheduleName\": \"개인 라이브 방송\",\n  \"scheduleDate\": \"2026-02-10\",\n  \"scheduleDetail\": \"저녁 8시 게임 방송 시작\",\n  \"scheduleType\": \"LIVE\",\n  \"creatorId\": null,\n  \"visitorIds\": null\n}"
                                    ),
                                    @ExampleObject(
                                            name = "4. 매니저 본인 일정 등록",
                                            summary = "매니저가 자신의 업무 미팅 등을 등록할 때",
                                            value = "{\n  \"scheduleName\": \"광고주 미팅\",\n  \"scheduleDate\": \"2026-02-05\",\n  \"scheduleDetail\": \"MCN 본사 3층 회의실\",\n  \"scheduleType\": \"MEETING\",\n  \"creatorId\": null,\n  \"visitorIds\": []\n}"
                                    )
                            }
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
