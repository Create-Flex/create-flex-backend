package com.mcn.in4.domain.creator.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreatorRequestDTO {

    // 크리에이터 생성 요청 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Create {
        @JsonProperty("member_name")
        private String memberName;

        @JsonProperty("creator_platform")
        private String creatorPlatform;

        @JsonProperty("creator_subscribe")
        private String creatorSubscribe;

        @JsonProperty("creator_category")
        private String creatorCategory;

        @JsonProperty("member_account")
        private String memberAccount;

        @JsonProperty("member_password")
        private String memberPassword;

        @JsonProperty("member_manager_id")
        private Long memberManagerId;

        @JsonProperty("creator_status")
        private String creatorStatus;

        @JsonProperty("creator_main_contact")
        private String creatorMainContact;
    }

    // 크리에이터 수정 요청 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Update {
        @JsonProperty("member_name")
        private String memberName;

        @JsonProperty("creator_platform")
        private String creatorPlatform;

        @JsonProperty("creator_subscribe")
        private String creatorSubscribe;

        @JsonProperty("creator_category")
        private String creatorCategory;

        @JsonProperty("member_account")
        private String memberAccount;

        @JsonProperty("member_password")
        private String memberPassword;

        @JsonProperty("member_manager_id")
        private Long memberManagerId;

        @JsonProperty("creator_status")
        private String creatorStatus;

        @JsonProperty("creator_main_contact")
        private String creatorMainContact;
    }
}