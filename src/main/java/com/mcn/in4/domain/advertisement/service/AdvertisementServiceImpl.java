package com.mcn.in4.domain.advertisement.service;

import com.mcn.in4.domain.advertisement.dto.request.AdvertisementRequestDTO;
import com.mcn.in4.domain.advertisement.dto.response.AdvertisementResponseDTO;
import com.mcn.in4.domain.advertisement.repository.AdvertisementRepository;
import com.mcn.in4.domain.advertisement.entity.CreatorPromotion;
import com.mcn.in4.domain.creator.entity.creatorEnum.PromotionStatus;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.repository.MemberRepository;
// import com.mcn.in4.entity.schedule.ScheduleRepository; 일정 구현 후 적용
import lombok.RequiredArgsConstructor;
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
    // private final ScheduleRepository scheduleRepository; 일정 구현 후 적용

    @Override
    @Transactional
    public Long createAdvertisement(AdvertisementRequestDTO.Create request) {
        // 크리에이터 존재 확인
        Member creator = memberRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "존재하지 않는 크리에이터입니다. creatorId: " + request.getCreatorId()));

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

        return savedPromotion.getPromotionId();
    }

    @Override
    public List<AdvertisementResponseDTO.Info> getMyAdvertisementsByFilter(Long managerId, String filter) {
        List<CreatorPromotion> promotions;

        switch (filter.toLowerCase()) {
            case "waiting":
                // 대기중인 제안 (WAITING 상태만)
                promotions = advertisementRepository.findByManagerIdAndStatus(managerId, PromotionStatus.WAITING);
                break;

            case "processed":
                // 처리내역 (ACCEPTED + REJECTED)
                promotions = advertisementRepository.findByManagerIdAndProcessedStatus(managerId);
                break;

            case "all":
            default:
                // 전체보기
                promotions = advertisementRepository.findByManagerId(managerId);
                break;
        }

        return promotions.stream()
                .map(AdvertisementResponseDTO.Info::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void acceptAdvertisement(Long promotionId) {
        CreatorPromotion promotion = findPromotion(promotionId);

        // 대기중 상태만 수락 가능
        if (promotion.getPromotionStatus() != PromotionStatus.WAITING) {
            throw new IllegalArgumentException("대기중 상태의 광고만 수락할 수 있습니다.");
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

        // 크리에이터 일정에 자동 추가 => 일정 구현 후 적용
        // createScheduleForPromotion(promotion);
    }

    @Override
    @Transactional
    public void rejectAdvertisement(Long promotionId) {
        CreatorPromotion promotion = findPromotion(promotionId);

        // 대기중 상태만 거절 가능
        if (promotion.getPromotionStatus() != PromotionStatus.WAITING) {
            throw new IllegalArgumentException("대기중 상태의 광고만 거절할 수 있습니다.");
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
    }

    // 광고 유효성 검사
    private CreatorPromotion findPromotion(Long promotionId) {
        return advertisementRepository.findByIdWithCreator(promotionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "존재하지 않는 광고 캠페인입니다. promotionId: " + promotionId));
    }

    // 광고 수락 시 크리에이터 일정에 자동 추가 => 일정 구현 후 적용
//    private void createScheduleForPromotion(CreatorPromotion promotion) {
//        Schedule schedule = Schedule.builder()
//                .member(promotion.getMemberCreator())
//                .scheduleName(promotion.getPromotionClient() + " - " + promotion.getPromotionName())
//                .scheduleDate(promotion.getPromotionTargerDate())
//                .scheduleDetail(promotion.getPromotionDetail())
//                .scheduleType(ScheduleType.PROMOTION)
//                .build();
//
//        scheduleRepository.save(schedule);
//    }
}