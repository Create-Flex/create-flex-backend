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

    // API: GET /api/employees/teams/{id}
    @GetMapping("/{id}")
    public ResponseEntity<List<MyTeamResponse>> getMyTeams(
            @PathVariable("id") Long memberId, // URL의 {id}를 받음
            @AuthenticationPrincipal String currentMemberId) { // [중요] 토큰의 subject(ID)를 String으로 받음

        if (currentMemberId == null) {
            return ResponseEntity.status(401).build();
        }

        // 토큰의 ID(String)와 요청한 ID(Long)를 서비스로 전달
        return ResponseEntity.ok(teamService.getMyTeams(currentMemberId, memberId));
    }
}