package com.mcn.in4.domain.contract.controller;

import com.mcn.in4.domain.contract.dto.request.ContractRequestDTO;
import com.mcn.in4.domain.contract.dto.response.ContractResponseDTO;
import com.mcn.in4.domain.contract.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    // 계약 등록
    @PostMapping
    public ResponseEntity<Map<String, Object>> createContract(
            @RequestBody ContractRequestDTO.Create request) {

        Long contractId = contractService.createContract(request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "계약이 성공적으로 등록되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 계약 목록 조회
    @GetMapping
    public ResponseEntity<List<ContractResponseDTO.Info>> getAllContracts() {
        return ResponseEntity.ok(contractService.getAllContracts());
    }

    /* 계약서 파일 직접 다운로드
    @GetMapping("/{contractId}/download")
    public ResponseEntity<Resource> downloadContractFile(
            @PathVariable Long contractId) {

        Resource file = contractService.downloadContractFile(contractId);
        String filename = contractService.getContractFilename(contractId);

        // 한글 파일명 인코딩
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + encodedFilename + "\"")
                .body(file);
    }
     */
}