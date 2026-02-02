package com.mcn.in4.domain.team.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MyTeamResponse {
    private Long teamId;
    private String teamName;
    private String teamDetail;
    private List<TeamMemberResponse> teamMembers;

    @Getter
    @AllArgsConstructor
    public static class TeamMemberResponse {
        private Long memberId;
        private String memberName;
        private String departmentName;
        private String task;
    }
}