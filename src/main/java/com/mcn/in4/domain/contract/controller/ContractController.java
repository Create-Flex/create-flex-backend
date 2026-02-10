package com.mcn.in4.domain.contract.controller;

import com.mcn.in4.domain.contract.controller.api.ContractApi;
import com.mcn.in4.domain.contract.dto.request.ContractRequestDTO;
import com.mcn.in4.domain.contract.dto.response.ContractResponseDTO;
import com.mcn.in4.domain.contract.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController implements ContractApi {

    private final ContractService contractService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ContractResponseDTO.Create> createContract(
            @RequestPart("request") ContractRequestDTO.Create request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        // DTO에 파일 설정 (기존 서비스 로직 유지)
        request.setFile(file);

        ContractResponseDTO.Create response = contractService.createContract(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 계약 목록 조회
    @Override
    @GetMapping
    public ResponseEntity<List<ContractResponseDTO.Info>> getAllContracts(
            @RequestParam(value = "name", required = false) String name) {
        return ResponseEntity.ok(contractService.getAllContracts(name));
    }
}