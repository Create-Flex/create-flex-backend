package com.mcn.in4.domain.creator.controller.api;

import com.mcn.in4.domain.creator.dto.request.CreatorWorkRequestDTO;
import com.mcn.in4.domain.creator.dto.response.CreatorWorkResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Tag(name = "크리에이터 업무현황", description = "크리에이터 업무현황 조회, 추가, 상태변경, 삭제를 담당하는 API입니다.")
public interface CreatorWorkApi {

    @Operation(
            summary = "크리에이터 업무 목록 조회",
            description = "특정 크리에이터의 업무 목록을 조회합니다.\n" +
                    "- 크리에이터 본인은 자신의 업무 목록 조회\n" +
                    "- 담당 매니저는 해당 크리에이터의 업무 목록 조회",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "404", description = "크리에이터를 찾을 수 없음")
            }
    )
    ResponseEntity<List<CreatorWorkResponseDTO.Info>> getWorks(
            @Parameter(description = "크리에이터 ID", example = "2001")
            @PathVariable Long creatorId
    );

    @Operation(
            summary = "크리에이터 업무 추가",
            description = "특정 크리에이터에 업무를 추가합니다.\n" +
                    "- 업무 상태는 기본 WORKING으로 생성\n" +
                    "- 작성자(member_worker_id)는 로그인한 사용자 ID로 등록",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "업무 추가 성공"),
                    @ApiResponse(responseCode = "404", description = "크리에이터를 찾을 수 없음")
            }
    )
    ResponseEntity<CreatorWorkResponseDTO.Info> createWork(
            @Parameter(description = "크리에이터 ID", example = "2001")
            @PathVariable Long creatorId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "업무 추가 정보",
                    content = @Content(examples = @ExampleObject(value = "{\n" +
                            "  \"workName\": \"새로운 콘텐츠 기획안\"\n" +
                            "}"))
            ) CreatorWorkRequestDTO.Create request,
            @Parameter(hidden = true) @AuthenticationPrincipal String userId
    );

    @Operation(
            summary = "크리에이터 업무 상태 변경",
            description = "특정 크리에이터의 업무 상태를 변경합니다 (WORKING ↔ DONE).",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "상태 변경 성공"),
                    @ApiResponse(responseCode = "404", description = "업무를 찾을 수 없음")
            }
    )
    ResponseEntity<CreatorWorkResponseDTO.Info> updateWorkStatus(
            @Parameter(description = "크리에이터 ID", example = "2001")
            @PathVariable Long creatorId,
            @Parameter(description = "업무 ID", example = "2001")
            @PathVariable Long workId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "변경할 업무 상태",
                    content = @Content(examples = @ExampleObject(value = "{\n" +
                            "  \"workStatus\": \"DONE\"\n" +
                            "}"))
            ) CreatorWorkRequestDTO.UpdateStatus request
    );

    @Operation(
            summary = "크리에이터 업무 삭제",
            description = "특정 크리에이터의 업무를 삭제합니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "업무를 찾을 수 없음")
            }
    )
    ResponseEntity<Map<String, Object>> deleteWork(
            @Parameter(description = "크리에이터 ID", example = "2001")
            @PathVariable Long creatorId,
            @Parameter(description = "업무 ID", example = "2001")
            @PathVariable Long workId
    );
}
