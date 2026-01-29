package com.mcn.in4.domain.employee.dto.requestDTO;

import com.mcn.in4.domain.member.entity.memberEnum.EmploymentType;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.entity.memberEnum.MemberStatus;
import lombok.*;

public class EmployeeRequestDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class EmployeeInsertRequestDto {
        // Member 엔티티 속 데이터
        private String memberAccount; // 아이디
        private String memberName; // 이름
        private MemberRole memberRole; // 권한
        private MemberStatus memberStatus; // 재직, 휴직
        private String task;// 직무
        private Integer departmentid; //부서 아이디
        private String password; // 비밀번호

        // MemberEmployeeDetail 엔티티 속 데이터
        private String nickname; //닉네임
        private String personalEmail; // 개인 계정
        private String personalCall; // 개인 전화번호
        private String address; // 주소
        private String engName;// 영어이름
        private String corporEmail; //사내 이메일
        private String hireDate; // 입사일
        private EmploymentType employmentType; // 입사유형  NEWBIE, EXPERIENCED
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor // 퇴사 사유
    public static class EmployeeQuitRequestDto {
        private String leavingReason;
    }
}
