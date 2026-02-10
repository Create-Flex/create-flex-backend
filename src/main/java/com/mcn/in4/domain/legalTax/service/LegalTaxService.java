package com.mcn.in4.domain.legalTax.service;

import com.mcn.in4.domain.legalTax.dto.request.LegalTaxRequestDTO;
import com.mcn.in4.domain.legalTax.dto.response.LegalTaxResponseDTO;
import com.mcn.in4.domain.legalTax.entity.creatorEnum.LegalTaxStatus;
import com.mcn.in4.domain.legalTax.entity.creatorEnum.LegalTaxType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LegalTaxService {

    // 법률/세무 상담 신청
    Long createLegalTax(LegalTaxRequestDTO.Create request);

    // 전체 상담 신청 목록 조회 (관리자용) - 필터 추가
    Page<LegalTaxResponseDTO.Info> getAllLegalTax(LegalTaxType type, String status, Pageable pageable);

    // 매니저가 담당하는 크리에이터들의 상담 신청 목록 조회 - 필터 추가
    Page<LegalTaxResponseDTO.Info> getMyLegalTax(Long managerId, LegalTaxType type, String status,
            Pageable pageable);

    // 상담 완료 처리
    void completeLegalTax(Long legalTaxId);
}