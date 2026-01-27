package com.mcn.in4.domain.creator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mcn.in4.entity.member.Member;
import com.mcn.in4.entity.member.MemberCreatorDetail;
import com.mcn.in4.entity.member.memberEnum.CreatorPlatform;
import com.mcn.in4.entity.member.memberEnum.CreatorStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreatorDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @JsonProperty("member_name")
        private String memberName;

        @JsonProperty("creator_platform")
        private CreatorPlatform creatorPlatform;

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
        private CreatorStatus creatorStatus;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
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

        public static Response from(Member creator, MemberCreatorDetail detail) {
            return Response.builder()
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

        public static Response fromWithProfile(Member creator, MemberCreatorDetail detail,
                                               String profileImage, String profileBanner) {
            return Response.builder()
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