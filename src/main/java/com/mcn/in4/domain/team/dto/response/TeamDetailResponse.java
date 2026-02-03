package com.mcn.in4.domain.team.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TeamDetailResponse {
    private Long teamId;
    private String teamName;
    private String teamDetail;
    private List<TeamMemberResponse> members;

    @Getter
    @AllArgsConstructor
    public static class TeamMemberResponse {
        private Long memberId;
        private String memberName;     // Member.memberName
        private String departmentName; // Department.departmentName
        private String task;           // Member.task (직책/직무)
        private String workStatus;     // 근무 상태 (출근, 휴가, 미출근 등)
    }
}