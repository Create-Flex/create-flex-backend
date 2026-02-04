package com.mcn.in4.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mcn.in4.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ManagerResponseDto {
    // 매니저 목록 조회 응답 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ManagerInfo {
        @JsonProperty("manager_id")
        private Long managerId;

        @JsonProperty("manager_name")
        private String managerName;

        @JsonProperty("manager_account")
        private String managerAccount;

        @JsonProperty("department_name")
        private String departmentName;

        public static ManagerInfo from(Member manager) {
            return ManagerInfo.builder()
                    .managerId(manager.getMemberId())
                    .managerName(manager.getMemberName())
                    .managerAccount(manager.getMemberAccount())
                    .departmentName(manager.getDepartment() != null ?
                            manager.getDepartment().getDepartmentName() : null)
                    .build();
        }
    }
}
