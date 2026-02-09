package com.mcn.in4.domain.contract.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mcn.in4.domain.contract.entity.CreatorContract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

public class ContractResponseDTO {

    // 계약 생성 응답 DTO (Presigned URL 포함)
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Create {
        @JsonProperty("contract_id")
        private Long contractId;

        @JsonProperty("presigned_url")
        private String presignedUrl;
    }

    // 계약 조회 응답 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Info {
        @JsonProperty("contract_id")
        private Long contractId;

        @JsonProperty("contract_name")
        private String contractName;

        @JsonProperty("creator_name")
        private String creatorName;

        @JsonProperty("contract_start")
        private LocalDate contractStart;

        @JsonProperty("contract_end")
        private LocalDate contractEnd;

        @JsonProperty("contract_file_url")
        private String contractFileUrl;

        public static Info from(CreatorContract contract) {
            return Info.builder()
                    .contractId(contract.getCreatorContractId())
                    .contractName(contract.getContractName())
                    .creatorName(contract.getCreatorName())
                    .contractStart(contract.getContractStart())
                    .contractEnd(contract.getContractEnd())
                    .contractFileUrl(contract.getContractFileUrl())
                    .build();
        }
    }
}