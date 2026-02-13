package com.mcn.in4.domain.advertisement.service;

import com.mcn.in4.domain.advertisement.dto.request.AdvertisementRequestDTO;
import com.mcn.in4.domain.advertisement.dto.response.AdvertisementResponseDTO;
import com.mcn.in4.domain.advertisement.repository.AdvertisementRepository;
import com.mcn.in4.domain.advertisement.entity.CreatorPromotion;
import com.mcn.in4.domain.creator.entity.creatorEnum.PromotionStatus;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.repository.MemberRepository;
import com.mcn.in4.domain.schedule.entity.Schedule;
import com.mcn.in4.domain.schedule.entity.scheduleEnum.ScheduleType;
import com.mcn.in4.domain.schedule.repository.SchedulRepository;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final MemberRepository memberRepository;
    private final SchedulRepository schedulRepository;
    private final com.mcn.in4.domain.notification.service.NotificationService notificationService;
    private final com.mcn.in4.domain.creator.repository.CreatorDetailRepository creatorDetailRepository;

    @Override
    @Transactional
    public Long createAdvertisement(AdvertisementRequestDTO.Create request, Long currentUserId) {
        // 크리에이터 존재 확인
        Member creator = memberRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new CustomException(ErrorCode.CREATOR_NOT_FOUND));

        // 광고 캠페인 엔티티 생성
        CreatorPromotion promotion = CreatorPromotion.builder()
                .memberCreator(creator)
                .promotionClient(request.getPromotionClient())
                .promotionName(request.getPromotionName())
                .promotionFee(request.getPromotionFee())
                .promotionDetail(request.getPromotionDetail())
                .createdAt(LocalDate.now())
                .promotionTargerDate(request.getPromotionTargetDate())
                .promotionStatus(PromotionStatus.WAITING)
                .build();

        CreatorPromotion savedPromotion = advertisementRepository.save(promotion);

        // 크리에이터와 담당 매니저에게 알림 전송
        try {
            Long managerId = creatorDetailRepository
                    .findByCreatorIdWithManager(creator.getMemberId())
                    .map(detail -> detail.getMemberManager() != null ? detail.getMemberManager().getMemberId() : null)
                    .orElse(null);

            notificationService.sendAdvertisementRegistrationNotification(
                    creator.getMemberName(),
                    request.getPromotionName(),
                    creator.getMemberId(),
                    managerId,
                    currentUserId);
        } catch (Exception e) {
            // 알림 전송 실패해도 등록은 정상 처리
        }

        return savedPromotion.getPromotionId();
    }

    @Override
    public Page<AdvertisementResponseDTO.Info> getMyAdvertisementsByFilter(
            Long managerId, String filter, Pageable pageable) {
        Page<CreatorPromotion> promotions;

        switch (filter.toLowerCase()) {
            case "waiting":
                // 대기중인 제안 (WAITING 상태만)
                promotions = advertisementRepository.findByManagerIdAndStatus(managerId, PromotionStatus.WAITING,
                        pageable);
                break;

            case "processed":
                // 처리내역 (ACCEPTED + REJECTED)
                promotions = advertisementRepository.findByManagerIdAndProcessedStatus(managerId, pageable);
                break;

            case "all":
            default:
                // 전체보기
                promotions = advertisementRepository.findByManagerId(managerId, pageable);
                break;
        }

        return promotions.map(AdvertisementResponseDTO.Info::from);
    }

    @Override
    @Transactional
    public void acceptAdvertisement(Long promotionId, Long currentUserId) {
        CreatorPromotion promotion = findPromotion(promotionId);

        // 대기중 상태만 수락 가능
        if (promotion.getPromotionStatus() != PromotionStatus.WAITING) {
            throw new CustomException(ErrorCode.INVALID_ADVERTISEMENT_STATUS);
        }

        // 상태를 ACCEPTED로 변경
        CreatorPromotion updatedPromotion = CreatorPromotion.builder()
                .promotionId(promotion.getPromotionId())
                .memberCreator(promotion.getMemberCreator())
                .promotionClient(promotion.getPromotionClient())
                .promotionName(promotion.getPromotionName())
                .promotionFee(promotion.getPromotionFee())
                .promotionDetail(promotion.getPromotionDetail())
                .createdAt(promotion.getCreatedAt())
                .promotionTargerDate(promotion.getPromotionTargerDate())
                .promotionStatus(PromotionStatus.ACCEPTED)
                .build();

        advertisementRepository.save(updatedPromotion);

        // 크리에이터 일정에 자동 추가
        createScheduleForPromotion(promotion);

        // 크리에이터와 담당 매니저에게 알림 전송
        try {
            Long managerId = creatorDetailRepository
                    .findByCreatorIdWithManager(promotion.getMemberCreator().getMemberId())
                    .map(detail -> detail.getMemberManager() != null ? detail.getMemberManager().getMemberId() : null)
                    .orElse(null);

            notificationService.sendAdvertisementAcceptanceNotification(
                    promotion.getMemberCreator().getMemberName(),
                    promotion.getPromotionName(),
                    promotion.getMemberCreator().getMemberId(),
                    managerId,
                    currentUserId);
        } catch (Exception e) {
            // 알림 전송 실패해도 수락은 정상 처리
        }
    }

    @Override
    @Transactional
    public void rejectAdvertisement(Long promotionId, Long currentUserId) {
        CreatorPromotion promotion = findPromotion(promotionId);

        // 대기중 상태만 거절 가능
        if (promotion.getPromotionStatus() != PromotionStatus.WAITING) {
            throw new CustomException(ErrorCode.INVALID_ADVERTISEMENT_STATUS);
        }

        // 상태를 REJECTED로 변경
        CreatorPromotion updatedPromotion = CreatorPromotion.builder()
                .promotionId(promotion.getPromotionId())
                .memberCreator(promotion.getMemberCreator())
                .promotionClient(promotion.getPromotionClient())
                .promotionName(promotion.getPromotionName())
                .promotionFee(promotion.getPromotionFee())
                .promotionDetail(promotion.getPromotionDetail())
                .createdAt(promotion.getCreatedAt())
                .promotionTargerDate(promotion.getPromotionTargerDate())
                .promotionStatus(PromotionStatus.REJECTED)
                .build();

        advertisementRepository.save(updatedPromotion);

        // 크리에이터와 담당 매니저에게 알림 전송
        try {
            Long managerId = creatorDetailRepository
                    .findByCreatorIdWithManager(promotion.getMemberCreator().getMemberId())
                    .map(detail -> detail.getMemberManager() != null ? detail.getMemberManager().getMemberId() : null)
                    .orElse(null);

            notificationService.sendAdvertisementRejectionNotification(
                    promotion.getMemberCreator().getMemberName(),
                    promotion.getPromotionName(),
                    promotion.getMemberCreator().getMemberId(),
                    managerId,
                    currentUserId);
        } catch (Exception e) {
            // 알림 전송 실패해도 거절은 정상 처리
        }
    }

    // 광고 유효성 검사
    private CreatorPromotion findPromotion(Long promotionId) {
        return advertisementRepository.findByIdWithCreator(promotionId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADVERTISEMENT_NOT_FOUND));
    }

    // 광고 수락 시 크리에이터 일정에 자동 추가
    private void createScheduleForPromotion(CreatorPromotion promotion) {
        Schedule schedule = Schedule.builder()
                .member(promotion.getMemberCreator()) // 일정 작성자
                .creator(promotion.getMemberCreator()) // 일정 대상 크리에이터
                .scheduleName(promotion.getPromotionClient())
                .scheduleDate(promotion.getPromotionTargerDate())
                .scheduleDetail(promotion.getPromotionDetail())
                .scheduleType(ScheduleType.PROMOTION)
                .build();

        schedulRepository.save(schedule);
    }
}