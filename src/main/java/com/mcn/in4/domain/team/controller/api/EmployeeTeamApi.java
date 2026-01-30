package com.mcn.in4.domain.team.controller.api;

import com.mcn.in4.domain.team.dto.response.MyTeamResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "팀 (직원)", description = "직원 전용 내 팀 조회 API")
public interface EmployeeTeamApi {

    @Operation(summary = "내 소속 팀 목록 조회", description = "로그인한 직원이 속해 있는 모든 팀의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패 (로그인 필요)")
    })
    ResponseEntity<List<MyTeamResponse>> getMyTeams(
            @Parameter(hidden = true) // Swagger UI에서 입력창 숨김
            @AuthenticationPrincipal String memberAccount
    );

    @Operation(summary = "내 팀 상세 정보 조회", description = "로그인한 직원이 속한 특정 팀의 상세 정보와 동료 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (본인이 속하지 않은 팀 조회 시)"),
            @ApiResponse(responseCode = "401", description = "인증 실패 (로그인 필요)")
    })
    ResponseEntity<MyTeamResponse> getMyTeamDetail(
            @Parameter(description = "조회할 팀의 ID", example = "1")
            @PathVariable("teamId") Long teamId,

            @Parameter(hidden = true)
            @AuthenticationPrincipal String memberAccount
    );
}