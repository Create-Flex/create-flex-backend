package com.mcn.in4.domain.team.dto.response;

import com.mcn.in4.domain.team.entity.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class TeamResponse {
    private Long teamId;
    private String teamName;
    private String teamDetail;
    private List<Long> memberIds;

    public TeamResponse(Team team) {
        this.teamId = team.getTeamId();
        this.teamName = team.getTeamName();
        this.teamDetail = team.getTeamDetail();
    }

    public TeamResponse(Team team, List<Long> memberIds) {
        this.teamId = team.getTeamId();
        this.teamName = team.getTeamName();
        this.teamDetail = team.getTeamDetail();
        this.memberIds = memberIds;
    }
}