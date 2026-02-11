package com.mcn.in4.domain.legalTax.service;

import com.mcn.in4.domain.creator.repository.CreatorDetailRepository;
import com.mcn.in4.domain.legalTax.dto.request.LegalTaxRequestDTO;
import com.mcn.in4.domain.legalTax.dto.response.LegalTaxResponseDTO;
import com.mcn.in4.domain.legalTax.entity.CreatorLegalTax;
import com.mcn.in4.domain.legalTax.entity.creatorEnum.LegalTaxStatus;
import com.mcn.in4.domain.legalTax.entity.creatorEnum.LegalTaxType;
import com.mcn.in4.domain.legalTax.repository.LegalTaxRepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.MemberCreatorDetail;
import com.mcn.in4.domain.member.repository.MemberRepository;
import com.mcn.in4.domain.notification.service.NotificationService;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LegalTaxServiceImpl implements LegalTaxService {

    private final LegalTaxRepository legalTaxRepository;
    private final MemberRepository memberRepository;
    private final CreatorDetailRepository creatorDetailRepository;
    private final NotificationService notificationService;

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
                .legalTaxStatus(LegalTaxStatus.NOT_RECEIVED)
                .build();

        CreatorLegalTax savedLegalTax = legalTaxRepository.save(legalTax);

        // 알림 전송: 관리자에게 법률/세무 등록 알림
        try {
            // 크리에이터 상세 정보에서 담당 매니저 조회
            MemberCreatorDetail creatorDetail = creatorDetailRepository
                    .findByCreatorIdWithManager(creator.getMemberId())
                    .orElse(null);

            String managerName = "크리에이터";
            if (creatorDetail != null && creatorDetail.getMemberManager() != null) {
                managerName = creatorDetail.getMemberManager().getMemberName();
            }

            // 법률인지 세무인지 한글로 변환
            String typeKorean = (legalTaxType == LegalTaxType.LEGAL) ? "법률" : "세무";

            // 알림 전송
            notificationService.sendLegalTaxRegistrationNotification(
                    managerName,
                    typeKorean,
                    LegalTaxResponseDTO.Info.from(savedLegalTax));
        } catch (Exception e) {
            // 알림 전송 실패해도 등록은 정상 처리
        }

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
            }
        }

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

        Page<CreatorLegalTax> legalTaxPage = legalTaxRepository.findByManagerIdWithFilters(managerId, type, status,
                excludeDone,
                pageable);

        return legalTaxPage.map(LegalTaxResponseDTO.Info::from);
    }

    @Override
    @Transactional
    public void completeLegalTax(Long legalTaxId) {
        CreatorLegalTax legalTax = findLegalTax(legalTaxId);

        CreatorLegalTax updatedLegalTax = CreatorLegalTax.builder()
                .legalTaxId(legalTax.getLegalTaxId())
                .memberCreator(legalTax.getMemberCreator())
                .legalTaxType(legalTax.getLegalTaxType())
                .legalTaxName(legalTax.getLegalTaxName())
                .legalTaxDetail(legalTax.getLegalTaxDetail())
                .legalTaxStatus(LegalTaxStatus.DONE)
                .build();

        legalTaxRepository.save(updatedLegalTax);

        // 알림 전송: 담당 매니저에게 완료 알림
        try {
            Member creator = legalTax.getMemberCreator();

            // 크리에이터 상세 정보에서 담당 매니저 조회
            MemberCreatorDetail creatorDetail = creatorDetailRepository
                    .findByCreatorIdWithManager(creator.getMemberId())
                    .orElse(null);

            if (creatorDetail != null && creatorDetail.getMemberManager() != null) {
                Member manager = creatorDetail.getMemberManager();
                String typeKorean = (legalTax.getLegalTaxType() == LegalTaxType.LEGAL) ? "법률" : "세무";

                notificationService.sendLegalTaxApprovalNotification(
                        manager,
                        creator.getMemberName(),
                        typeKorean,
                        LegalTaxResponseDTO.Info.from(updatedLegalTax));
            }
        } catch (Exception e) {
            // 알림 전송 실패해도 수정은 정상 처리
        }
    }

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