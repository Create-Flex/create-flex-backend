package com.mcn.in4.domain.member.dto;

import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberProfileResponseDto {
    // 공통 정보 (Member & MemberProfile)
    private Long memberId;
    private String memberName;
    private String memberAccount;
    private MemberRole memberRole;
    private String profileImage;
    private String profileBanner;

    // 직원 상세 정보 (MemberEmployeeDetail)
    // 직원이 아닌 경우 null
    private String task; // 직무
    private String nickname; // 닉네임
    private String departmentName; // 부서명
    private String engName; // 영문명
    private String personalEmail; // 개인 이메일
    private String personalCall; // 개인 휴대전화
    private String hireDate; // 입사일
    private String address; // 주소
    private Double vacationRemainder; // 잔여 연차
}
