package com.mcn.in4.domain.contract.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mcn.in4.domain.contract.entity.CreatorContract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class ContractResponseDTO {

    // 계약 생성 응답 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Create {
        @JsonProperty("contract_id")
        private Long contractId;
    }

    // 계약 조회 응답 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Info {
        @JsonProperty("contract_id")
        private Long contractId;

        @JsonProperty("contract_name")
        private String contractName;

        @JsonProperty("creator_id")
        private Long creatorId;

        @JsonProperty("creator_name")
        private String creatorName;

        @JsonProperty("contract_start")
        private LocalDate contractStart;

        @JsonProperty("contract_end")
        private LocalDate contractEnd;

        @JsonProperty("contract_file_url")
        private String contractFileUrl;

        @JsonProperty("is_active")
        private Boolean isActive;

        public static Info from(CreatorContract contract) {
            LocalDate today = LocalDate.now();
            boolean isActive = !today.isBefore(contract.getContractStart())
                    && !today.isAfter(contract.getContractEnd());

            return Info.builder()
                    .contractId(contract.getCreatorContractId())
                    .contractName(contract.getContractName())
                    .creatorId(contract.getMemberCreator().getMemberId())
                    .creatorName(contract.getMemberCreator().getMemberName())
                    .contractStart(contract.getContractStart())
                    .contractEnd(contract.getContractEnd())
                    .contractFileUrl(contract.getContractFileUrl())
                    .isActive(isActive)
                    .build();
        }
    }
}