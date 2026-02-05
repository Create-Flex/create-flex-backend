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
        private Integer departmentid; // 부서 아이디
        private String password; // 비밀번호

        // MemberEmployeeDetail 엔티티 속 데이터
        private String nickname; // 닉네임
        private String personalEmail; // 개인 계정
        private String personalCall; // 개인 전화번호
        private String address; // 주소
        private String engName;// 영어이름
        private String corporEmail; // 사내 이메일
        private String hireDate; // 입사일
        private EmploymentType employmentType; // 입사유형 NEWBIE, EXPERIENCED
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor // 퇴사 사유
    public static class EmployeeQuitRequestDto {
        private String leavingReason;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class EmployeeUpdateRequestDto {
        // Member 엔티티 속 데이터
        private String memberName; // 이름
        private MemberRole memberRole; // 권한
        private MemberStatus memberStatus; // 재직, 휴직
        private String task;// 직무
        private Integer departmentid; // 부서 아이디
        private String password; // 비밀번호
        // MemberEmployeeDetail 엔티티 속 데이터
        private String nickname; // 닉네임
        private String personalEmail; // 개인 계정
        private String personalCall; // 개인 전화번호
        private String address; // 주소
        private String engName;// 영어이름
        private String corporEmail; // 사내 이메일
        private String hireDate; // 입사일
        private EmploymentType employmentType; // 입사유형 NEWBIE, EXPERIENCED
    }

    // 마이페이지 프로필 수정 DTO (본인만 수정 가능한 항목)
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MyProfileUpdateDto {
        private String nickname; // 닉네임
        private String personalEmail; // 개인 이메일
        private String personalCall; // 휴대전화
        private String address; // 주소
        private String engName; // 영문 이름
    }

    // 비밀번호 변경 DTO
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PasswordChangeDto {
        private String currentPassword; // 현재 비밀번호
        private String newPassword; // 새 비밀번호
    }
}
