package com.mcn.in4.domain.team.service;

import com.mcn.in4.domain.team.dto.request.TeamCreateRequest;
import com.mcn.in4.domain.team.dto.request.TeamMemberUpdateRequest;
import com.mcn.in4.domain.team.dto.response.TeamDetailResponse;
import com.mcn.in4.domain.team.dto.response.TeamResponse;
import java.util.List;

public interface TeamService {
    List<TeamResponse> findAllTeams();
    TeamDetailResponse getTeamDetail(Long teamId);
    void createTeam(TeamCreateRequest request);
    void updateTeamMembers(Long teamId, TeamMemberUpdateRequest request);
    void deleteTeam(Long teamId);
}