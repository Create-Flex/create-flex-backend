package com.mcn.in4.domain.creator.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.MemberCreatorDetail;
import com.mcn.in4.domain.member.entity.memberEnum.CreatorPlatform;
import com.mcn.in4.domain.member.entity.memberEnum.CreatorStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreatorResponseDTO {

    // 크리에이터 생성 응답 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Create {
        @JsonProperty("creator_id")
        private Long creatorId;
    }

    // 크리에이터 조회 응답 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Info {
        @JsonProperty("member_id")
        private Long memberId;

        @JsonProperty("member_name")
        private String memberName;

        @JsonProperty("member_account")
        private String memberAccount;

        @JsonProperty("creator_platform")
        private CreatorPlatform creatorPlatform;

        @JsonProperty("creator_subscribe")
        private String creatorSubscribe;

        @JsonProperty("creator_category")
        private String creatorCategory;

        @JsonProperty("creator_status")
        private CreatorStatus creatorStatus;

        @JsonProperty("manager_id")
        private Long managerId;

        @JsonProperty("manager_name")
        private String managerName;

        @JsonProperty("profile_image")
        private String profileImage;

        @JsonProperty("profile_banner")
        private String profileBanner;

        public static Info from(Member creator, MemberCreatorDetail detail) {
            return Info.builder()
                    .memberId(creator.getMemberId())
                    .memberName(creator.getMemberName())
                    .memberAccount(creator.getMemberAccount())
                    .creatorPlatform(detail.getCreatorPlatform())
                    .creatorSubscribe(detail.getCreatorSubscribe())
                    .creatorCategory(detail.getCreatorCategory())
                    .creatorStatus(detail.getCreatorStatus())
                    .managerId(detail.getMemberManager().getMemberId())
                    .managerName(detail.getMemberManager().getMemberName())
                    .build();
        }

        public static Info fromWithProfile(Member creator, MemberCreatorDetail detail,
                                           String profileImage, String profileBanner) {
            return Info.builder()
                    .memberId(creator.getMemberId())
                    .memberName(creator.getMemberName())
                    .memberAccount(creator.getMemberAccount())
                    .creatorPlatform(detail.getCreatorPlatform())
                    .creatorSubscribe(detail.getCreatorSubscribe())
                    .creatorCategory(detail.getCreatorCategory())
                    .creatorStatus(detail.getCreatorStatus())
                    .managerId(detail.getMemberManager().getMemberId())
                    .managerName(detail.getMemberManager().getMemberName())
                    .profileImage(profileImage)
                    .profileBanner(profileBanner)
                    .build();
        }
    }
}