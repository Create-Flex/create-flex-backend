package com.mcn.in4.domain.team.controller;

import com.mcn.in4.domain.team.dto.request.TeamCreateRequest;
import com.mcn.in4.domain.team.dto.request.TeamMemberUpdateRequest;
import com.mcn.in4.domain.team.dto.response.MyTeamResponse;
import com.mcn.in4.domain.team.dto.response.TeamDetailResponse;
import com.mcn.in4.domain.team.dto.response.TeamResponse;
import com.mcn.in4.domain.team.service.TeamService; // 인터페이스 참조
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/teams")
public class TeamController {

    private final TeamService teamService; // TeamServiceImpl이 아닌 Interface 주입

    @GetMapping
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        return ResponseEntity.ok(teamService.findAllTeams());
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDetailResponse> getTeamDetail(@PathVariable("teamId") Long teamId) {
        return ResponseEntity.ok(teamService.getTeamDetail(teamId));
    }

    @PostMapping
    public ResponseEntity<String> createTeam(@RequestBody TeamCreateRequest request) {
        teamService.createTeam(request);
        return ResponseEntity.ok("팀이 성공적으로 생성되었습니다.");
    }

    @PatchMapping("/{teamId}")
    public ResponseEntity<String> updateTeamMembers(
            @PathVariable("teamId") Long teamId,
            @RequestBody TeamMemberUpdateRequest request) {

        teamService.updateTeamMembers(teamId, request);
        return ResponseEntity.ok("팀 멤버 정보가 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<String> deleteTeam(@PathVariable("teamId") Long teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.ok("팀이 성공적으로 삭제되었습니다.");
    }
}