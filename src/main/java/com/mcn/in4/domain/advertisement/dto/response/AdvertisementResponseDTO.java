package com.mcn.in4.domain.advertisement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mcn.in4.domain.advertisement.entity.CreatorPromotion;
import com.mcn.in4.domain.creator.entity.creatorEnum.PromotionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class AdvertisementResponseDTO {

    // 광고 캠페인 생성 응답 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Create {
        @JsonProperty("promotion_id")
        private Long promotionId;
    }

    // 광고 캠페인 조회 응답 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Info {
        @JsonProperty("promotion_id")
        private Long promotionId;

        @JsonProperty("creator_id")
        private Long creatorId;

        @JsonProperty("creator_name")
        private String creatorName;

        @JsonProperty("promotion_client")
        private String promotionClient;

        @JsonProperty("promotion_name")
        private String promotionName;

        @JsonProperty("promotion_fee")
        private String promotionFee;

        @JsonProperty("promotion_detail")
        private String promotionDetail;

        @JsonProperty("created_at")
        private LocalDate createdAt;

        @JsonProperty("promotion_target_date")
        private LocalDate promotionTargetDate;

        @JsonProperty("promotion_status")
        private PromotionStatus promotionStatus;

        public static Info from(CreatorPromotion promotion) {
            return Info.builder()
                    .promotionId(promotion.getPromotionId())
                    .creatorId(promotion.getMemberCreator().getMemberId())
                    .creatorName(promotion.getMemberCreator().getMemberName())
                    .promotionClient(promotion.getPromotionClient())
                    .promotionName(promotion.getPromotionName())
                    .promotionFee(promotion.getPromotionFee())
                    .promotionDetail(promotion.getPromotionDetail())
                    .createdAt(promotion.getCreatedAt())
                    .promotionTargetDate(promotion.getPromotionTargerDate())
                    .promotionStatus(promotion.getPromotionStatus())
                    .build();
        }
    }
}