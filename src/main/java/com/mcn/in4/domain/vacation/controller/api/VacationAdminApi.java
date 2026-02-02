package com.mcn.in4.domain.vacation.controller.api;

import com.mcn.in4.domain.vacation.dto.request.VacationRejectRequestDTO;
import com.mcn.in4.domain.vacation.dto.response.AdminVacationListResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationStatisticsResponseDTO;
import com.mcn.in4.domain.vacation.entity.enums.VacationApprove;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "휴가 관리 (관리자)", description = "휴가 승인/반려, 전체 목록 조회, 통계를 담당하는 API입니다. 관리자 권한이 필요합니다.")
public interface VacationAdminApi {

    @Operation(
            summary = "전체 휴가 목록 조회",
            description = "전체 직원의 휴가 신청 목록을 조회합니다. 기간, 상태, 이름으로 필터링 가능합니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 없음 (관리자만 접근 가능)")
            }
    )
    ResponseEntity<List<AdminVacationListResponseDTO>> getVacationList(
            @Parameter(description = "조회 시작일 (기본: 오늘-1개월)", example = "2026-01-01")
            @RequestParam(required = false) LocalDate startDate,
            @Parameter(description = "조회 종료일 (기본: 오늘)", example = "2026-01-31")
            @RequestParam(required = false) LocalDate endDate,
            @Parameter(description = "승인 상태 필터 (APPROVE_NEED, APPROVED, REJECTED)")
            @RequestParam(required = false) VacationApprove status,
            @Parameter(description = "직원 이름 검색", example = "홍길동")
            @RequestParam(required = false) String name,
            @Parameter(hidden = true) Authentication authentication
    );

    @Operation(
            summary = "휴가 통계 조회",
            description = "이번달 휴가자 수, 미승인 대기자 수, 이번달 병가자 수를 조회합니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 없음 (관리자만 접근 가능)")
            }
    )
    ResponseEntity<VacationStatisticsResponseDTO> getVacationStatistics(
            @Parameter(hidden = true) Authentication authentication
    );

    @Operation(
            summary = "휴가 승인",
            description = "승인 대기 상태의 휴가를 승인합니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "승인 성공"),
                    @ApiResponse(responseCode = "400", description = "승인 대기 상태가 아님"),
                    @ApiResponse(responseCode = "404", description = "휴가를 찾을 수 없음"),
                    @ApiResponse(responseCode = "403", description = "권한 없음 (관리자만 접근 가능)")
            }
    )
    ResponseEntity<Void> approveVacation(
            @Parameter(description = "휴가 ID", example = "1")
            @PathVariable Long vacationId,
            @Parameter(hidden = true) Authentication authentication
    );

    @Operation(
            summary = "휴가 반려",
            description = "승인 대기 상태의 휴가를 반려합니다. 연차/반차의 경우 잔여 연차가 복구됩니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "반려 성공"),
                    @ApiResponse(responseCode = "400", description = "승인 대기 상태가 아님"),
                    @ApiResponse(responseCode = "404", description = "휴가를 찾을 수 없음"),
                    @ApiResponse(responseCode = "403", description = "권한 없음 (관리자만 접근 가능)")
            }
    )
    ResponseEntity<Void> rejectVacation(
            @Parameter(description = "휴가 ID", example = "1")
            @PathVariable Long vacationId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "반려 사유",
                    content = @Content(examples = @ExampleObject(value = "{\n" +
                            "  \"rejectReason\": \"업무 일정 충돌로 인해 반려합니다.\"\n" +
                            "}"))
            ) VacationRejectRequestDTO request,
            @Parameter(hidden = true) Authentication authentication
    );
}
