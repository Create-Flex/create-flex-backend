package com.mcn.in4.domain.creator.controller.api;

import com.mcn.in4.domain.creator.dto.request.CreatorRequestDTO;
import com.mcn.in4.domain.creator.dto.response.CreatorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Tag(name = "크리에이터 관리", description = "크리에이터 등록, 조회, 수정, 삭제를 담당하는 API입니다.")
public interface CreatorApi {

    @Operation(
            summary = "크리에이터 등록",
            description = "새로운 크리에이터를 등록합니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "크리에이터 등록 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    ResponseEntity<Map<String, Object>> createCreator(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "크리에이터 등록 정보",
                    content = @Content(examples = @ExampleObject(value = "{\n" +
                            "  \"member_name\": \"신규크리에이터\",\n" +
                            "  \"creator_platform\": \"YOUTUBE\",\n" +
                            "  \"creator_subscribe\": \"500000\",\n" +
                            "  \"creator_category\": \"게임\",\n" +
                            "  \"member_account\": \"newcreator01\",\n" +
                            "  \"member_password\": \"password123\",\n" +
                            "  \"member_manager_id\": 1003,\n" +
                            "  \"creator_status\": \"ACTIVE\",\n" +
                            "  \"creator_main_contact\": \"010-1234-5678\"\n" +
                            "}"))
            ) CreatorRequestDTO.Create request
    );

    @Operation(
            summary = "크리에이터 목록 조회",
            description = "전체 크리에이터 목록을 조회합니다. 이름으로 검색 가능합니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    ResponseEntity<List<CreatorResponseDTO.Info>> getAllCreators(
            @Parameter(description = "검색할 크리에이터 이름 (선택)", example = "감스트")
            @RequestParam(required = false) String name
    );

    @Operation(
            summary = "크리에이터 상세 조회",
            description = "특정 크리에이터의 상세 정보를 조회합니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "404", description = "크리에이터를 찾을 수 없음"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    ResponseEntity<CreatorResponseDTO.Info> getCreatorById(
            @Parameter(description = "크리에이터 ID", example = "2001")
            @PathVariable Long creatorId
    );

    @Operation(
            summary = "크리에이터 정보 수정",
            description = "크리에이터의 정보를 수정합니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "수정 성공"),
                    @ApiResponse(responseCode = "404", description = "크리에이터를 찾을 수 없음"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    ResponseEntity<Map<String, Object>> updateCreator(
            @Parameter(description = "크리에이터 ID", example = "2001")
            @PathVariable Long creatorId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정할 크리에이터 정보",
                    content = @Content(examples = @ExampleObject(value = "{\n" +
                            "  \"member_name\": \"감스트\",\n" +
                            "  \"creator_subscribe\": \"1300000\",\n" +
                            "  \"creator_status\": \"ACTIVE\"\n" +
                            "}"))
            ) CreatorRequestDTO.Update request
    );

    @Operation(
            summary = "크리에이터 삭제",
            description = "크리에이터를 삭제합니다 (상태를 SUSPENDED로 변경).",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "크리에이터를 찾을 수 없음"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    ResponseEntity<Map<String, Object>> deleteCreator(
            @Parameter(description = "크리에이터 ID", example = "2001")
            @PathVariable Long creatorId
    );

    @Operation(
            summary = "매니저별 크리에이터 조회",
            description = "특정 매니저가 담당하는 크리에이터 목록을 조회합니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "404", description = "매니저를 찾을 수 없음")
            }
    )
    ResponseEntity<List<CreatorResponseDTO.Info>> getMyCreators(
            @Parameter(description = "매니저 ID", example = "1003")
            @PathVariable Long managerId
    );
}