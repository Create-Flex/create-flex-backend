package com.mcn.in4.domain.advertisement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class AdvertisementRequestDTO {

    // 광고 캠페인 등록 요청 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Create {
        @JsonProperty("creator_id")
        private Long creatorId;

        @JsonProperty("promotion_client")
        private String promotionClient;

        @JsonProperty("promotion_name")
        private String promotionName;

        @JsonProperty("promotion_fee")
        private String promotionFee;

        @JsonProperty("promotion_detail")
        private String promotionDetail;

        @JsonProperty("promotion_target_date")
        private LocalDate promotionTargetDate;
    }
}