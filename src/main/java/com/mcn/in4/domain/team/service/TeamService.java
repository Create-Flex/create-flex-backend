package com.mcn.in4.domain.team.service;

import com.mcn.in4.domain.team.dto.response.TeamResponse;
import java.util.List;

public interface TeamService {
    List<TeamResponse> findAllTeams();
}