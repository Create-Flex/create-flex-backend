package com.mcn.in4.domain.contract.controller.api;

import com.mcn.in4.domain.contract.dto.request.ContractRequestDTO;
import com.mcn.in4.domain.contract.dto.response.ContractResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "계약 관리", description = "크리에이터 계약 등록 및 조회를 담당하는 API입니다.")
public interface ContractApi {

        @Operation(summary = "계약 등록 (파일 업로드)", description = """
                        새로운 크리에이터 계약을 등록하고 계약서 파일을 업로드합니다.

                        동작 방식:
                        1. 계약 정보와 파일을 multipart/form-data로 전송
                        2. 서버가 S3 업로드용 Presigned URL 생성 및 계약 정보 저장
                        3. 클라이언트는 반환된 presigned_url로 파일을 PUT 요청으로 업로드
                        4. 업로드 완료 후 계약서는 S3에 저장되고 contract_file_url로 조회 가능
                        """, security = @SecurityRequirement(name = "JWT"), responses = {
                        @ApiResponse(responseCode = "201", description = "계약 등록 성공 및 Presigned URL 반환", content = @Content(schema = @Schema(implementation = ContractResponseDTO.Create.class))),
                        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터 (계약 시작일이 종료일보다 이후인 경우)"),
                        @ApiResponse(responseCode = "403", description = "권한 없음 (관리자 전용)")
        })
        ResponseEntity<ContractResponseDTO.Create> createContract(
                        @org.springframework.web.bind.annotation.RequestPart("request") ContractRequestDTO.Create request,
                        @org.springframework.web.bind.annotation.RequestPart(value = "file", required = false) org.springframework.web.multipart.MultipartFile file);

        @Operation(summary = "계약 목록 조회", description = """
                        전체 크리에이터 계약 목록을 조회합니다.

                        - 계약 시작일 기준 내림차순으로 정렬
                        - 각 계약의 contract_file_url을 통해 S3에 저장된 계약서 조회 가능
                        """, security = @SecurityRequirement(name = "JWT"), responses = {
                        @ApiResponse(responseCode = "200", description = "조회 성공"),
                        @ApiResponse(responseCode = "403", description = "권한 없음 (관리자 전용)")
        })
        ResponseEntity<List<ContractResponseDTO.Info>> getAllContracts();
}