package com.mcn.in4.domain.team.dto.response;

import com.mcn.in4.domain.team.entity.Team;
import lombok.Getter;

@Getter
public class TeamResponse {
    private Long teamId;
    private String teamName;
    private String teamDetail;

    public TeamResponse(Team team) {
        this.teamId = team.getTeamId();
        this.teamName = team.getTeamName();
        this.teamDetail = team.getTeamDetail();
    }
}