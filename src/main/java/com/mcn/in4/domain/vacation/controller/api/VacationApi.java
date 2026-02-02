package com.mcn.in4.domain.vacation.controller.api;

import com.mcn.in4.domain.vacation.dto.request.VacationRequestDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationDetailResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationListResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationRemainderResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationResponseDTO;
import com.mcn.in4.domain.vacation.entity.enums.VacationType;
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

@Tag(name = "휴가 관리 (사용자)", description = "휴가 신청, 조회, 잔여 연차 확인을 담당하는 API입니다.")
public interface VacationApi {

    @Operation(
            summary = "휴가 신청",
            description = "새로운 휴가를 신청합니다. 크리에이터는 사용할 수 없습니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "휴가 신청 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 (잔여 연차 부족 등)"),
                    @ApiResponse(responseCode = "403", description = "권한 없음 (크리에이터)")
            }
    )
    ResponseEntity<VacationResponseDTO> createVacation(
            @Parameter(description = "휴가 유형", example = "ANNUAL")
            @RequestParam("type") String type,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "휴가 신청 정보",
                    content = @Content(examples = {
                            @ExampleObject(name = "연차 (ANNUAL)", value = "{\n" +
                                    "  \"memberId\": 1001,\n" +
                                    "  \"vacationStart\": \"2026-02-01\",\n" +
                                    "  \"vacationEnd\": \"2026-02-03\",\n" +
                                    "  \"vacationDetail\": \"개인 사유\"\n" +
                                    "}"),
                            @ExampleObject(name = "반차 (HALF)", value = "{\n" +
                                    "  \"memberId\": 1001,\n" +
                                    "  \"vacationStart\": \"2026-02-01\",\n" +
                                    "  \"vacationEnd\": \"2026-02-01\",\n" +
                                    "  \"vacationDetail\": \"오전 반차\"\n" +
                                    "}"),
                            @ExampleObject(name = "경조사 (FAMILY)", value = "{\n" +
                                    "  \"memberId\": 1001,\n" +
                                    "  \"vacationStart\": \"2026-02-01\",\n" +
                                    "  \"vacationEnd\": \"2026-02-03\",\n" +
                                    "  \"vacationDetail\": \"결혼식 참석\",\n" +
                                    "  \"familyRelation\": \"형제\",\n" +
                                    "  \"familyDetail\": \"결혼\"\n" +
                                    "}"),
                            @ExampleObject(name = "병가 (SICK)", value = "{\n" +
                                    "  \"memberId\": 1001,\n" +
                                    "  \"vacationStart\": \"2026-02-01\",\n" +
                                    "  \"vacationEnd\": \"2026-02-02\",\n" +
                                    "  \"vacationDetail\": \"감기 몸살\",\n" +
                                    "  \"sickDetail\": \"고열 및 몸살 증상\",\n" +
                                    "  \"sickHospital\": \"서울대학교병원\"\n" +
                                    "}"),
                            @ExampleObject(name = "워케이션 (WORKATION)", value = "{\n" +
                                    "  \"memberId\": 1001,\n" +
                                    "  \"vacationStart\": \"2026-02-01\",\n" +
                                    "  \"vacationEnd\": \"2026-02-05\",\n" +
                                    "  \"vacationDetail\": \"제주도 워케이션\",\n" +
                                    "  \"workationWhere\": \"제주도 서귀포시\",\n" +
                                    "  \"workationContact\": \"010-1234-5678\",\n" +
                                    "  \"workationPlan\": \"오전 업무 / 오후 자유시간\",\n" +
                                    "  \"workationHandover\": \"긴급 건은 홍길동 대리에게 인계\"\n" +
                                    "}")
                    })
            ) VacationRequestDTO request,
            @Parameter(hidden = true) Authentication authentication
    );

    @Operation(
            summary = "내 휴가 목록 조회",
            description = "로그인한 사용자의 휴가 사용 내역을 조회합니다. 기간 및 유형 필터 적용 가능합니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 없음 (크리에이터)")
            }
    )
    ResponseEntity<List<VacationListResponseDTO>> getMyVacations(
            @Parameter(description = "회원 ID", example = "1001")
            @RequestParam("memberId") Long memberId,
            @Parameter(description = "조회 시작일 (기본: 오늘-1개월)", example = "2026-01-01")
            @RequestParam(required = false) LocalDate startDate,
            @Parameter(description = "조회 종료일 (기본: 오늘+1개월)", example = "2026-02-28")
            @RequestParam(required = false) LocalDate endDate,
            @Parameter(description = "휴가 유형 필터 (ANNUAL, HALF, FAMILY, SICK, WORKATION)")
            @RequestParam(required = false) VacationType type,
            @Parameter(hidden = true) Authentication authentication
    );

    @Operation(
            summary = "휴가 상세 조회",
            description = "특정 휴가의 상세 정보를 조회합니다. 휴가 유형에 따라 추가 정보가 포함됩니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "404", description = "휴가를 찾을 수 없음"),
                    @ApiResponse(responseCode = "403", description = "권한 없음 (크리에이터)")
            }
    )
    ResponseEntity<VacationDetailResponseDTO> getVacationDetail(
            @Parameter(description = "휴가 ID", example = "1")
            @PathVariable Long vacationId,
            @Parameter(hidden = true) Authentication authentication
    );

    @Operation(
            summary = "내 잔여 연차 조회",
            description = "로그인한 사용자의 잔여 연차 정보를 조회합니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 없음 (크리에이터)")
            }
    )
    ResponseEntity<VacationRemainderResponseDTO> getMyVacationRemainder(
            @Parameter(description = "회원 ID", example = "1001")
            @RequestParam("memberId") Long memberId,
            @Parameter(hidden = true) Authentication authentication
    );
}
