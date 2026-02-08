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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@Tag(name = "일정관리", description = "일정 관리 API")
public interface ScheduleApi {
        @Operation(summary = "나의 일정 등록", description = "일정의 성격(합방, 개인, 대행 등)에 따라 적절한 파라미터를 전송합니다.", responses = {
                        @ApiResponse(responseCode = "200", description = "일정 등록 성공"),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터")
        })
        @PostMapping("/")
        ResponseEntity<String> createSchedule(
                        @Parameter(hidden = true) String userId,
                        @Parameter(hidden = true) Authentication authentication,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "상황별 일정 등록 예시", required = true, content = @Content(mediaType = "application/json", examples = {
                                        @ExampleObject(name = "1. 합방(MERGE) 일정 등록", summary = "매니저가 크리에이터 합방 일정을 등록할 때", value = "{\n  \"scheduleName\": \"감스트 x 또간집 콜라보 먹방\",\n  \"scheduleDate\": \"2026-02-15\",\n  \"scheduleDetail\": \"강남역 인근 맛집 탐방 및 합방 진행\",\n  \"scheduleType\": \"MERGE\",\n  \"creatorId\": 2001,\n  \"visitorIds\": [2002, 2005]\n}"),
                                        @ExampleObject(name = "2. 매니저의 크리에이터 일정 대행 등록", summary = "매니저가 담당 크리에이터의 단독 일정을 등록할 때", value = "{\n  \"scheduleName\": \"갤럭시 S26 홍보 영상 촬영\",\n  \"scheduleDate\": \"2026-02-20\",\n  \"scheduleDetail\": \"삼성전자 본사 스튜디오 촬영\",\n  \"scheduleType\": \"PROMOTION\",\n  \"creatorId\": 2001,\n  \"visitorIds\": []\n}"),
                                        @ExampleObject(name = "3. 크리에이터 본인 일정 등록", summary = "크리에이터가 직접 자신의 일정을 등록할 때", value = "{\n  \"scheduleName\": \"개인 라이브 방송\",\n  \"scheduleDate\": \"2026-02-10\",\n  \"scheduleDetail\": \"저녁 8시 게임 방송 시작\",\n  \"scheduleType\": \"LIVE\",\n  \"creatorId\": null,\n  \"visitorIds\": null\n}"),
                                        @ExampleObject(name = "4. 관리자 - 회사 일정 등록 (COMPANY)", summary = "관리자가 회사의 중요 일정을 등록할 때 (모든 직원이 볼 수 있음)", value = "{\n  \"scheduleName\": \"전사 워크샵\",\n  \"scheduleDate\": \"2026-03-15\",\n  \"scheduleDetail\": \"2026년 상반기 전사 워크샵 (가평 연수원)\",\n  \"scheduleType\": \"COMPANY\",\n  \"creatorId\": null,\n  \"visitorIds\": []\n}"),
                                        @ExampleObject(name = "5. 관리자 - 개인 업무 등록 (PERSONAL)", summary = "관리자가 자신의 개인적인 업무를 등록할 때", value = "{\n  \"scheduleName\": \"외부 투자자 미팅\",\n  \"scheduleDate\": \"2026-02-20\",\n  \"scheduleDetail\": \"강남 파이낸스센터 10층 미팅\",\n  \"scheduleType\": \"PERSONAL\",\n  \"creatorId\": null,\n  \"visitorIds\": []\n}"),
                                        @ExampleObject(name = "6. 매니저 - 개인 업무 등록 (PERSONAL)", summary = "매니저가 자신의 업무를 등록할 때 (매니저는 본인 업무는 PERSONAL만 가능)", value = "{\n  \"scheduleName\": \"개인 연차\",\n  \"scheduleDate\": \"2026-02-25\",\n  \"scheduleDetail\": \"개인 사유로 인한 연차 사용\",\n  \"scheduleType\": \"PERSONAL\",\n  \"creatorId\": null,\n  \"visitorIds\": []\n}")
                        })) @RequestBody ScheduleRequestDTO.ScheduleCreateRequestDto requestDto);

        @Operation(summary = "나의 월간 일정 조회", description = "특정 월을 기준으로 본인이 관련된 모든 일정을 조회합니다.", responses = {
                        @ApiResponse(responseCode = "200", description = "조회 성공")
        })
        @GetMapping("/me")
        ResponseEntity<List<SchedulReponseDTO.ScheduleResponseDto>> getMyMonthlySchedules(
                        @Parameter(hidden = true) String userId,
                        @Parameter(description = "조회 년월 (YYYY-MM)", example = "2026-02") @RequestParam("month") String month);

        @Operation(summary = "일정 수정", description = "기존 일정을 수정합니다. 일정 타입이 변경될 경우, 방문자(Visitor) 정보가 그에 맞게 초기화되거나 갱신될 수 있습니다.", responses = {
                        @ApiResponse(responseCode = "200", description = "수정 성공"),
                        @ApiResponse(responseCode = "403", description = "수정 권한 없음 (본인 일정 또는 관리자의 회사 일정만 수정 가능)")
        })
        @PatchMapping("/{scheduleId}")
        ResponseEntity<String> updateSchedule(
                        @Parameter(hidden = true) String userId,
                        @Parameter(hidden = true) Authentication authentication,
                        @Parameter(description = "일정 ID", required = true) @PathVariable("scheduleId") Long scheduleId,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수정할 일정 정보 (타입 변경 시 visitorIds 처리 주의)", required = true, content = @Content(mediaType = "application/json", examples = {
                                        @ExampleObject(name = "1. 콘텐츠 -> 합방(MERGE)으로 변경", summary = "일반 일정을 합방으로 변경하며 방문자 추가", value = "{\n  \"scheduleName\": \"급하게 잡힌 합방\",\n  \"scheduleDate\": \"2026-02-15\",\n  \"scheduleDetail\": \"갑자기 합방하기로 함\",\n  \"scheduleType\": \"MERGE\",\n  \"creatorId\": 2001,\n  \"visitorIds\": [2002, 2005]\n}"),
                                        @ExampleObject(name = "2. 합방 -> 기타(PERSONAL)로 변경", summary = "합방이 취소되어 개인 일정으로 변경 (방문자 자동 삭제됨)", value = "{\n  \"scheduleName\": \"합방 취소 - 개인 휴식\",\n  \"scheduleDate\": \"2026-02-15\",\n  \"scheduleDetail\": \"합방 취소됨\",\n  \"scheduleType\": \"ETC\",\n  \"creatorId\": null,\n  \"visitorIds\": []\n}"),
                                        @ExampleObject(name = "3. 개인일정 단순 수정", summary = "타입 변경 없이 제목/내용만 수정", value = "{\n  \"scheduleName\": \"수정된 제목\",\n  \"scheduleDate\": \"2026-02-15\",\n  \"scheduleDetail\": \"내용 보완\",\n  \"scheduleType\": \"PERSONAL\",\n  \"creatorId\": null,\n  \"visitorIds\": []\n}")
                        })) @RequestBody ScheduleRequestDTO.ScheduleUpdateRequestDto requestDto);

        @Operation(summary = "일정 삭제", description = "일정을 삭제합니다. 본인 일정 또는 관리자의 회사 일정만 삭제 가능합니다.", responses = {
                        @ApiResponse(responseCode = "200", description = "삭제 성공"),
                        @ApiResponse(responseCode = "403", description = "삭제 권한 없음")
        })
        @DeleteMapping("/{scheduleId}")
        ResponseEntity<String> deleteSchedule(
                        @Parameter(hidden = true) String userId,
                        @Parameter(hidden = true) Authentication authentication,
                        @Parameter(description = "일정 ID", required = true) @PathVariable("scheduleId") Long scheduleId);
}
