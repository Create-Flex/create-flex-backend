package com.mcn.in4.domain.legalTax.service;

import com.mcn.in4.domain.creator.repository.CreatorDetailRepository;
import com.mcn.in4.domain.legalTax.dto.request.LegalTaxRequestDTO;
import com.mcn.in4.domain.legalTax.dto.response.LegalTaxResponseDTO;
import com.mcn.in4.domain.legalTax.entity.CreatorLegalTax;
import com.mcn.in4.domain.legalTax.entity.creatorEnum.LegalTaxStatus;
import com.mcn.in4.domain.legalTax.entity.creatorEnum.LegalTaxType;
import com.mcn.in4.domain.legalTax.repository.LegalTaxRepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.repository.MemberRepository;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LegalTaxServiceImpl implements LegalTaxService {

    private final LegalTaxRepository legalTaxRepository;
    private final MemberRepository memberRepository;
    private final CreatorDetailRepository creatorDetailRepository;

    @Override
    @Transactional
    public Long createLegalTax(LegalTaxRequestDTO.Create request) {
        // 크리에이터 존재 확인
        Member creator = memberRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new CustomException(ErrorCode.CREATOR_NOT_FOUND));

        // 상담 유형 검증
        LegalTaxType legalTaxType = parseLegalTaxType(request.getLegalTaxType());

        // 법률/세무 상담 신청 엔티티 생성
        CreatorLegalTax legalTax = CreatorLegalTax.builder()
                .memberCreator(creator)
                .legalTaxType(legalTaxType)
                .legalTaxName(request.getLegalTaxName())
                .legalTaxDetail(request.getLegalTaxDetail())
                .legalTaxStatus(LegalTaxStatus.NOT_RECEIVED) // 초기 상태: 접수 대기
                .build();

        CreatorLegalTax savedLegalTax = legalTaxRepository.save(legalTax);

        return savedLegalTax.getLegalTaxId();
    }

    @Override
    public Page<LegalTaxResponseDTO.Info> getAllLegalTax(LegalTaxType type, String statusStr, Pageable pageable) {
        LegalTaxStatus status = null;
        boolean excludeDone = false;

        if ("NOT_DONE".equals(statusStr)) {
            excludeDone = true;
        } else if (statusStr != null && !statusStr.isEmpty()) {
            try {
                status = LegalTaxStatus.valueOf(statusStr);
            } catch (IllegalArgumentException e) {
                // 유효하지 않은 status는 null로 처리
                // 여기서는 무시하고 전체 조회
            }
        }

        // 항상 필터 메서드 사용 (null이면 전체 조회)
        Page<CreatorLegalTax> legalTaxPage = legalTaxRepository.findAllWithFilters(type, status, excludeDone, pageable);

        return legalTaxPage.map(LegalTaxResponseDTO.Info::from);
    }

    @Override
    public Page<LegalTaxResponseDTO.Info> getMyLegalTax(Long managerId, LegalTaxType type, String statusStr,
            Pageable pageable) {
        LegalTaxStatus status = null;
        boolean excludeDone = false;

        if ("NOT_DONE".equals(statusStr)) {
            excludeDone = true;
        } else if (statusStr != null && !statusStr.isEmpty()) {
            try {
                status = LegalTaxStatus.valueOf(statusStr);
            } catch (IllegalArgumentException e) {
                // Ignore invalid status
            }
        }

        // 항상 필터 메서드 사용 (null이면 전체 조회)
        Page<CreatorLegalTax> legalTaxPage = legalTaxRepository.findByManagerIdWithFilters(managerId, type, status,
                excludeDone,
                pageable);

        return legalTaxPage.map(LegalTaxResponseDTO.Info::from);
    }

    @Override
    @Transactional
    public void completeLegalTax(Long legalTaxId) {
        CreatorLegalTax legalTax = findLegalTax(legalTaxId);

        // 상태를 DONE으로 변경
        CreatorLegalTax updatedLegalTax = CreatorLegalTax.builder()
                .legalTaxId(legalTax.getLegalTaxId())
                .memberCreator(legalTax.getMemberCreator())
                .legalTaxType(legalTax.getLegalTaxType())
                .legalTaxName(legalTax.getLegalTaxName())
                .legalTaxDetail(legalTax.getLegalTaxDetail())
                .legalTaxStatus(LegalTaxStatus.DONE)
                .build();

        legalTaxRepository.save(updatedLegalTax);
    }

    // 상담신청 유효성 검사
    private LegalTaxType parseLegalTaxType(String type) {
        try {
            return LegalTaxType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private CreatorLegalTax findLegalTax(Long legalTaxId) {
        return legalTaxRepository.findById(legalTaxId)
                .orElseThrow(() -> new CustomException(ErrorCode.LEGALTAX_NOT_FOUND));
    }
}