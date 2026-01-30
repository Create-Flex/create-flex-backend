package com.mcn.in4.domain.team.controller;

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
public class EmployeeTeamController {

    private final TeamService teamService;

    // 1. 내 팀 목록 조회 (수정됨)
    // URL: GET /api/employees/teams  <-- {id} 제거!
    // 토큰만 있으면 누군지 아니까 ID를 URL에 안 넣어도 됩니다.
    @GetMapping
    public ResponseEntity<List<MyTeamResponse>> getMyTeams(
            @AuthenticationPrincipal String memberAccount) {

        if (memberAccount == null) {
            return ResponseEntity.status(401).build();
        }

        // 서비스에 사번만 넘기면 알아서 찾아줌
        return ResponseEntity.ok(teamService.getMyTeams(memberAccount));
    }

    // 2. 특정 팀 상세 조회
    // URL: GET /api/employees/teams/{teamId}
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