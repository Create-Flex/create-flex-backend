package com.mcn.in4.domain.employee.dto.responseDTO;

import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.entity.memberEnum.MemberStatus;
import lombok.*;

public class EmployeeResponseDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class EmployeeDetailResponseDto {
        // Member 엔티티 속 데이터
        private String memberAccount;
        private String memberName;
        private MemberRole memberRole;
        private MemberStatus memberStatus;
        private String task;
        private String departmentName;
        // MemberEmployeeDetail 엔티티 속 데이터
        private String nickname;
        private String personalEmail;
        private String personalCall;
        private String address;
        private String engName;
    }
}
