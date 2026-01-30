package com.mcn.in4.domain.advertisement.controller.api;

import com.mcn.in4.domain.advertisement.dto.request.AdvertisementRequestDTO;
import com.mcn.in4.domain.advertisement.dto.response.AdvertisementResponseDTO;
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

@Tag(name = "광고 캠페인 관리", description = "크리에이터 광고 캠페인 등록, 조회 및 승인/거절을 담당하는 API입니다.")
public interface AdvertisementApi {

    @Operation(
            summary = "광고 캠페인 등록",
            description = "새로운 광고 캠페인을 등록합니다. 초기 상태는 WAITING(대기중)입니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "광고 캠페인 등록 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
                    @ApiResponse(responseCode = "404", description = "크리에이터를 찾을 수 없음")
            }
    )
    ResponseEntity<Map<String, Object>> createAdvertisement(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "광고 캠페인 등록 정보",
                    content = @Content(examples = @ExampleObject(value = "{\n" +
                            "  \"creator_id\": 2001,\n" +
                            "  \"promotion_client\": \"LG전자\",\n" +
                            "  \"promotion_name\": \"LG 그램 2026 신제품 리뷰\",\n" +
                            "  \"promotion_fee\": \"7000000\",\n" +
                            "  \"promotion_detail\": \"LG 그램 신모델 상세 리뷰 영상 제작, 성능 테스트 및 비교 분석 포함\",\n" +
                            "  \"promotion_target_date\": \"2026-03-15\"\n" +
                            "}"))
            ) AdvertisementRequestDTO.Create request
    );

    @Operation(
            summary = "내 담당 크리에이터 광고 캠페인 목록 조회 (매니저용)",
            description = "로그인한 매니저가 담당하는 크리에이터들의 광고 캠페인 목록을 조회합니다. 필터를 통해 상태별 조회가 가능합니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 없음")
            }
    )
    ResponseEntity<List<AdvertisementResponseDTO.Info>> getMyAdvertisements(
            @Parameter(hidden = true) String userId,
            @Parameter(
                    description = "필터 조건: all(전체보기), waiting(대기중인 제안), processed(처리내역)",
                    example = "all"
            )
            @RequestParam(required = false, defaultValue = "all") String filter
    );

    @Operation(
            summary = "광고 캠페인 상태 변경",
            description = "광고 캠페인을 수락(ACCEPTED) 또는 거절(REJECTED)합니다. 수락 시 일정이 자동으로 추가됩니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "상태 변경 성공"),
                    @ApiResponse(responseCode = "400", description = "유효하지 않은 상태 값 또는 대기중 상태가 아님"),
                    @ApiResponse(responseCode = "404", description = "광고 캠페인을 찾을 수 없음")
            }
    )
    ResponseEntity<Map<String, Object>> updateAdvertisementStatus(
            @Parameter(description = "광고 캠페인 ID", example = "2001")
            @PathVariable Long id,
            @Parameter(
                    description = "변경할 상태 (ACCEPTED: 수락, REJECTED: 거절)",
                    example = "ACCEPTED"
            )
            @RequestParam String status
    );
}