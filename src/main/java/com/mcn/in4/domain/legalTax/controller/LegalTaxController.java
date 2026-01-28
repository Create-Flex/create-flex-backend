package com.mcn.in4.domain.legalTax.controller;

import com.mcn.in4.domain.legalTax.dto.request.LegalTaxRequestDTO;
import com.mcn.in4.domain.legalTax.dto.response.LegalTaxResponseDTO;
import com.mcn.in4.domain.legalTax.service.LegalTaxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/legaltax")
@RequiredArgsConstructor
public class LegalTaxController {

    private final LegalTaxService legalTaxService;

    // 법률/세무 상담 신청
    @PostMapping
    public ResponseEntity<Map<String, Object>> createLegalTax(
            @RequestBody LegalTaxRequestDTO.Create request) {
        Long legalTaxId = legalTaxService.createLegalTax(request);


        Map<String, Object> response = new HashMap<>();
        response.put("message", "상담 신청이 성공적으로 등록되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // 전체 상담 신청 목록 조회 (관리자용)
    @GetMapping("/admin/all")
    public ResponseEntity<List<LegalTaxResponseDTO.Info>> getAllLegalTax() {
        return ResponseEntity.ok(legalTaxService.getAllLegalTax());
    }


    // 내 담당 크리에이터의 상담 신청 목록 조회 (매니저용)
    @GetMapping("/my")
    public ResponseEntity<List<LegalTaxResponseDTO.Info>> getMyLegalTax(
            @AuthenticationPrincipal String userId) {

        Long managerId = Long.parseLong(userId);
        return ResponseEntity.ok(legalTaxService.getMyLegalTax(managerId));
    }

    // 상담 완료 처리(관리자)
    @PatchMapping("/{legalTaxId}/complete")
    public ResponseEntity<Map<String, Object>> completeLegalTax(
            @PathVariable Long legalTaxId) {

        legalTaxService.completeLegalTax(legalTaxId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "상담이 완료 처리되었습니다.");

        return ResponseEntity.ok(response);
    }
}