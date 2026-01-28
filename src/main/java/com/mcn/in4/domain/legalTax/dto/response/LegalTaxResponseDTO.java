package com.mcn.in4.domain.legalTax.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mcn.in4.domain.legalTax.entity.CreatorLegalTax;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LegalTaxResponseDTO {

    // 상담 신청 생성 응답 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Create {
        @JsonProperty("legal_tax_id")
        private Long legalTaxId;
    }

    // 상담 신청 조회 응답 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Info {
        @JsonProperty("legal_tax_id")
        private Long legalTaxId;

        @JsonProperty("creator_id")
        private Long creatorId;

        @JsonProperty("creator_name")
        private String creatorName;

        @JsonProperty("legal_tax_type")
        private String legalTaxType;

        @JsonProperty("legal_tax_name")
        private String legalTaxName;

        @JsonProperty("legal_tax_detail")
        private String legalTaxDetail;

        @JsonProperty("legal_tax_status")
        private String legalTaxStatus;

        public static Info from(CreatorLegalTax legalTax) {
            return Info.builder()
                    .legalTaxId(legalTax.getLegalTaxId())
                    .creatorId(legalTax.getMemberCreator().getMemberId())
                    .creatorName(legalTax.getMemberCreator().getMemberName())
                    .legalTaxType(legalTax.getLegalTaxType().name())
                    .legalTaxName(legalTax.getLegalTaxName())
                    .legalTaxDetail(legalTax.getLegalTaxDetail())
                    .legalTaxStatus(legalTax.getLegalTaxStatus().name())
                    .build();
        }
    }
}