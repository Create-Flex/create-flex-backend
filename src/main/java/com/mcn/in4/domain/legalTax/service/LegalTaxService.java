package com.mcn.in4.domain.legalTax.service;

import com.mcn.in4.domain.legalTax.dto.request.LegalTaxRequestDTO;
import com.mcn.in4.domain.legalTax.dto.response.LegalTaxResponseDTO;

import java.util.List;

public interface LegalTaxService {

    // 법률/세무 상담 신청
    Long createLegalTax(LegalTaxRequestDTO.Create request);

    // 전체 상담 신청 목록 조회 (관리자용)
    List<LegalTaxResponseDTO.Info> getAllLegalTax();

    // 매니저가 담당하는 크리에이터들의 상담 신청 목록 조회
    List<LegalTaxResponseDTO.Info> getMyLegalTax(Long managerId);

    // 상담 완료 처리
    void completeLegalTax(Long legalTaxId);
}