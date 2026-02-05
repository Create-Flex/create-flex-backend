package com.mcn.in4.domain.team.controller.api;

import com.mcn.in4.domain.team.dto.request.TeamCreateRequest;
import com.mcn.in4.domain.team.dto.request.TeamMemberUpdateRequest;
import com.mcn.in4.domain.team.dto.response.TeamDetailResponse;
import com.mcn.in4.domain.team.dto.response.TeamResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "팀 (관리자)", description = "관리자 전용 팀 관리 API (생성, 조회, 수정, 삭제)")
public interface TeamApi {

        @Operation(summary = "전체 팀 목록 조회", description = "등록된 모든 팀의 기본 정보 목록을 조회합니다.")
        @ApiResponse(responseCode = "200", description = "조회 성공")
        ResponseEntity<List<TeamResponse>> getAllTeams();

        @Operation(summary = "팀 상세 정보 조회", description = "팀 ID를 통해 팀의 상세 정보와 소속된 팀원 목록을 조회합니다.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "조회 성공"),
                        @ApiResponse(responseCode = "404", description = "존재하지 않는 팀 ID", content = @Content(schema = @Schema(hidden = true)))
        })
        ResponseEntity<TeamDetailResponse> getTeamDetail(
                        @Parameter(description = "조회할 팀의 ID", example = "1") @PathVariable("teamId") Long teamId);

        @Operation(summary = "신규 팀 생성", description = "새로운 팀을 생성하고 초기 멤버를 배정합니다.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "생성 성공"),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(hidden = true)))
        })
        ResponseEntity<Long> createTeam(
                        @RequestBody TeamCreateRequest request);

        @Operation(summary = "팀 정보 수정", description = "특정 팀의 기본 정보(이름, 설명)를 수정합니다.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "수정 성공"),
                        @ApiResponse(responseCode = "404", description = "존재하지 않는 팀 ID", content = @Content(schema = @Schema(hidden = true)))
        })
        ResponseEntity<String> updateTeamInfo(
                        @Parameter(description = "수정할 팀의 ID", example = "1") @PathVariable("teamId") Long teamId,
                        @RequestBody TeamCreateRequest request);

        @Operation(summary = "팀원 구성 수정", description = "특정 팀의 소속 멤버를 수정합니다. (기존 멤버는 제외되고 요청된 멤버들로 교체됩니다.)")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "수정 성공"),
                        @ApiResponse(responseCode = "404", description = "팀 또는 멤버를 찾을 수 없음", content = @Content(schema = @Schema(hidden = true)))
        })
        ResponseEntity<String> updateTeamMembers(
                        @Parameter(description = "수정할 팀의 ID", example = "1") @PathVariable("teamId") Long teamId,
                        @RequestBody TeamMemberUpdateRequest request);

        @Operation(summary = "팀 삭제", description = "특정 팀을 삭제합니다. 팀에 속한 멤버 연결 정보도 함께 제거됩니다.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "삭제 성공"),
                        @ApiResponse(responseCode = "404", description = "존재하지 않는 팀", content = @Content(schema = @Schema(hidden = true)))
        })
        ResponseEntity<String> deleteTeam(
                        @Parameter(description = "삭제할 팀의 ID", example = "1") @PathVariable("teamId") Long teamId);
}