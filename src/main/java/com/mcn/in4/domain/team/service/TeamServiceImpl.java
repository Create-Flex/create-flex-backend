package com.mcn.in4.domain.team.service;

import com.mcn.in4.domain.team.dto.response.TeamDetailResponse;
import com.mcn.in4.domain.team.dto.response.TeamResponse;
import com.mcn.in4.domain.team.entity.Team;
import com.mcn.in4.domain.team.entity.TeamRelay;
import com.mcn.in4.domain.team.repository.TeamRelayRepository;
import com.mcn.in4.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamRelayRepository teamRelayRepository;

    @Override
    public List<TeamResponse> findAllTeams() {
        return teamRepository.findAll().stream()
                .map(TeamResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public TeamDetailResponse getTeamDetail(Long teamId) {
        // 1. 팀 정보 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("해당 팀을 찾을 수 없습니다."));

        // 2. 소속 멤버 상세 조회 (Fetch Join 활용)
        List<TeamRelay> relays = teamRelayRepository.findAllByTeamIdWithMemberAndDept(teamId);

        // 3. DTO 변환
        List<TeamDetailResponse.TeamMemberResponse> memberResponses = relays.stream()
                .map(tr -> {
                    var m = tr.getMember();
                    String deptName = (m.getDepartment() != null) ? m.getDepartment().getDepartmentName() : "무소속";
                    return new TeamDetailResponse.TeamMemberResponse(
                            m.getMemberId(), m.getMemberName(), deptName, m.getTask()
                    );
                }).toList();

        return TeamDetailResponse.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .teamDetail(team.getTeamDetail())
                .members(memberResponses)
                .build();
    }
}