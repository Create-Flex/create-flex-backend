package com.mcn.in4.domain.legalTax.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LegalTaxRequestDTO {

    // 법률/세무 상담 신청 요청 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Create {
        @JsonProperty("creator_id")
        private Long creatorId;

        @JsonProperty("legal_tax_type")
        private String legalTaxType; // "LEGAL" or "TAX"

        @JsonProperty("legal_tax_name")
        private String legalTaxName;

        @JsonProperty("legal_tax_detail")
        private String legalTaxDetail;
    }
}