package com.mcn.in4.domain.contract.controller.api;

import com.mcn.in4.domain.contract.dto.request.ContractRequestDTO;
import com.mcn.in4.domain.contract.dto.response.ContractResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Tag(name = "계약 관리", description = "크리에이터 계약 등록 및 조회를 담당하는 API입니다.")
public interface ContractApi {

    @Operation(
            summary = "계약 등록",
            description = "새로운 크리에이터 계약을 등록합니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "계약 등록 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터 (계약 시작일이 종료일보다 이후인 경우)"),
                    @ApiResponse(responseCode = "403", description = "권한 없음 (관리자 전용)")
            }
    )
    ResponseEntity<Map<String, Object>> createContract(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "계약 등록 정보",
                    content = @Content(examples = @ExampleObject(value = "{\n" +
                            "  \"contract_name\": \"신규크리에이터 전속 계약\",\n" +
                            "  \"creator_name\": \"신규크리에이터\",\n" +
                            "  \"contract_start\": \"2026-02-01\",\n" +
                            "  \"contract_end\": \"2029-01-31\",\n" +
                            "  \"contract_file_url\": \"https://cdn.mcn.com/contracts/new_creator_2026.pdf\"\n" +
                            "}"))
            ) ContractRequestDTO.Create request
    );

    @Operation(
            summary = "계약 목록 조회",
            description = "전체 크리에이터 계약 목록을 조회합니다. 계약 시작일 기준 내림차순으로 정렬됩니다.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "403", description = "권한 없음 (관리자 전용)")
            }
    )
    ResponseEntity<List<ContractResponseDTO.Info>> getAllContracts();
}