package com.mcn.in4.domain.legalTax.controller.api;

import com.mcn.in4.domain.legalTax.dto.request.LegalTaxRequestDTO;
import com.mcn.in4.domain.legalTax.dto.response.LegalTaxResponseDTO;
import com.mcn.in4.domain.legalTax.entity.creatorEnum.LegalTaxStatus;
import com.mcn.in4.domain.legalTax.entity.creatorEnum.LegalTaxType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Tag(name = "법률/세무 관리", description = "크리에이터 법률 및 세무 상담 신청 및 관리를 담당하는 API입니다.")
public interface LegalTaxApi {

        @Operation(summary = "법률/세무 상담 신청", description = "크리에이터가 법률 또는 세무 상담을 신청합니다.", security = @SecurityRequirement(name = "JWT"), responses = {
                        @ApiResponse(responseCode = "201", description = "상담 신청 성공"),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
                        @ApiResponse(responseCode = "404", description = "크리에이터를 찾을 수 없음")
        })
        ResponseEntity<Map<String, Object>> createLegalTax(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "법률/세무 상담 신청 정보", content = @Content(examples = @ExampleObject(value = "{\n"
                                        +
                                        "  \"creator_id\": 2001,\n" +
                                        "  \"legal_tax_type\": \"TAX\",\n" +
                                        "  \"legal_tax_name\": \"2025년 2분기 부가세 신고\",\n" +
                                        "  \"legal_tax_detail\": \"2분기 유튜브 광고 수익 및 협찬 관련 부가세 신고 필요\"\n" +
                                        "}"))) LegalTaxRequestDTO.Create request);

        @Operation(summary = "전체 상담 신청 목록 조회 (관리자용)", description = "모든 법률/세무 상담 신청 목록을 조회합니다. 유형 및 상태로 필터링 가능합니다.", security = @SecurityRequirement(name = "JWT"), responses = {
                        @ApiResponse(responseCode = "200", description = "조회 성공"),
                        @ApiResponse(responseCode = "403", description = "권한 없음 (관리자 전용)")
        })
        ResponseEntity<Page<LegalTaxResponseDTO.Info>> getAllLegalTax(
                        @Parameter(description = "상담 유형 필터 (LEGAL/TAX)", example = "TAX") @RequestParam(required = false) LegalTaxType type,
                        @Parameter(description = "상담 상태 필터 (NOT_RECEIVED/CONTACTED/IN_PROGRESS/DONE/NOT_DONE)", example = "IN_PROGRESS") @RequestParam(required = false) String status,
                        @PageableDefault(size = 8) Pageable pageable);

        @Operation(summary = "내 담당 크리에이터 상담 신청 목록 조회 (매니저용)", description = "로그인한 매니저가 담당하는 크리에이터들의 법률/세무 상담 신청 목록을 조회합니다.", security = @SecurityRequirement(name = "JWT"), responses = {
                        @ApiResponse(responseCode = "200", description = "조회 성공"),
                        @ApiResponse(responseCode = "403", description = "권한 없음")
        })
        ResponseEntity<Page<LegalTaxResponseDTO.Info>> getMyLegalTax(
                        @Parameter(hidden = true) String userId,
                        @Parameter(description = "상담 유형 필터 (LEGAL/TAX)", example = "LEGAL") @RequestParam(required = false) LegalTaxType type,
                        @Parameter(description = "상담 상태 필터 (NOT_RECEIVED/CONTACTED/IN_PROGRESS/DONE/NOT_DONE)", example = "CONTACTED") @RequestParam(required = false) String status,
                        @PageableDefault(size = 8) Pageable pageable);

        @Operation(summary = "상담 완료 처리 (관리자용)", description = "법률/세무 상담을 완료 상태로 변경합니다.", security = @SecurityRequirement(name = "JWT"), responses = {
                        @ApiResponse(responseCode = "200", description = "완료 처리 성공"),
                        @ApiResponse(responseCode = "404", description = "상담 신청을 찾을 수 없음"),
                        @ApiResponse(responseCode = "403", description = "권한 없음 (관리자 전용)")
        })
        ResponseEntity<Map<String, Object>> completeLegalTax(
                        @Parameter(description = "상담 신청 ID", example = "2001") @PathVariable Long legalTaxId);
}