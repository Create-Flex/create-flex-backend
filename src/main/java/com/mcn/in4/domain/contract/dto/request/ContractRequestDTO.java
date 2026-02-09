package com.mcn.in4.domain.contract.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class ContractRequestDTO {

    // 계약 등록 요청 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Create {
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

        private MultipartFile file;
    }
}