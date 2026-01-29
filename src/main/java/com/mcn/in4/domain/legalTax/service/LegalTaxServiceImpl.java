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
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(() -> new IllegalArgumentException(
                        "존재하지 않는 크리에이터입니다. creatorId: " + request.getCreatorId()));

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
    public List<LegalTaxResponseDTO.Info> getAllLegalTax(LegalTaxType type, LegalTaxStatus status) {
        // 항상 필터 메서드 사용 (null이면 전체 조회)
        List<CreatorLegalTax> legalTaxList = legalTaxRepository.findAllWithFilters(type, status);

        return legalTaxList.stream()
                .map(LegalTaxResponseDTO.Info::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<LegalTaxResponseDTO.Info> getMyLegalTax(Long managerId, LegalTaxType type, LegalTaxStatus status) {
        // 항상 필터 메서드 사용 (null이면 전체 조회)
        List<CreatorLegalTax> legalTaxList = legalTaxRepository.findByManagerIdWithFilters(managerId, type, status);

        return legalTaxList.stream()
                .map(LegalTaxResponseDTO.Info::from)
                .collect(Collectors.toList());
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
            throw new IllegalArgumentException("유효하지 않은 상담 유형입니다: " + type);
        }
    }

    private CreatorLegalTax findLegalTax(Long legalTaxId) {
        return legalTaxRepository.findById(legalTaxId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "존재하지 않는 상담 신청입니다. legalTaxId: " + legalTaxId));
    }
}