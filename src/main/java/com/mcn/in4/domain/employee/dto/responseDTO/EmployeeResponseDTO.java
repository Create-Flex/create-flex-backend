package com.mcn.in4.domain.employee.dto.responseDTO;

import com.mcn.in4.domain.member.entity.memberEnum.EmploymentType;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.entity.memberEnum.MemberStatus;
import lombok.*;

import java.util.List;

public class EmployeeResponseDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class EmployeeDetailResponseDto {
        // Member 엔티티 데이터
        private String memberAccount;    // 아이디
        private String memberName;       // 이름
        private MemberRole memberRole;   // 권한
        private MemberStatus memberStatus; // 재직, 휴직
        private String task;             // 직무
        private Integer departmentid;    // 부서 아이디
        // MemberEmployeeDetail 엔티티 데이터
        private String nickname;         // 닉네임
        private String personalEmail;    // 개인 계정
        private String personalCall;     // 개인 전화번호
        private String address;          // 주소
        private String engName;          // 영어이름
        private String corporEmail;      // 사내 이메일
        private String hireDate;         // 입사일
        private EmploymentType employmentType; // 입사유형 (NEWBIE, EXPERIENCED)
    }

    /* 직원 관리 페이지 전체 응답 DTO */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmployeeManagementResponseDto {
        private EmployeeSummaryDto summary;      // 요약 통계
        private List<EmployeeListDto> list;       // 직원 리스트
    }
    /* 상단 요약 통계 DTO */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmployeeSummaryDto { //여긴 대시보드(통계)
        private long totalCount;        // 총 직원 수
        private long workingCount;      // 현재 근무 중 (근무중, 출근)
        private long vacationCount;     // 휴가/부재 (연차, 반차, 병가 등)
        private long newHireCount;      // 신규 입사자 (1년 이내)
    }
    /* 직원 리스트 개별 항목 DTO */
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmployeeListDto {
        private String memberName;      // 이름
        private String departmentName;  // 부서
        private String task;            // 직무
        private String memberAccount;   // 사번
        private String corporEmail;     // 사내 이메일
        private String personalCall;    // 개인 연락처
        private String hireDate;        // 입사일
        private String attendanceStatus;// 현재 상태 (근태)
    }
}
