package com.mcn.in4.domain.team.controller;

import com.mcn.in4.domain.team.controller.api.EmployeeTeamApi;
import com.mcn.in4.domain.team.dto.response.MyTeamResponse;
import com.mcn.in4.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees/teams")
public class EmployeeTeamController implements EmployeeTeamApi { // 인터페이스 구현

    private final TeamService teamService;

    @Override
    @GetMapping
    public ResponseEntity<List<MyTeamResponse>> getMyTeams(
            @AuthenticationPrincipal String memberAccount) {

        if (memberAccount == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(teamService.getMyTeams(memberAccount));
    }

    @Override
    @GetMapping("/{teamId}")
    public ResponseEntity<MyTeamResponse> getMyTeamDetail(
            @PathVariable("teamId") Long teamId,
            @AuthenticationPrincipal String memberAccount) {

        if (memberAccount == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(teamService.getMyTeamDetail(memberAccount, teamId));
    }
}