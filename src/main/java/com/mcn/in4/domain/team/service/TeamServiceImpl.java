package com.mcn.in4.domain.team.service;

import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.repository.MemberRepository;
import com.mcn.in4.domain.team.dto.request.TeamCreateRequest;
import com.mcn.in4.domain.team.dto.request.TeamMemberUpdateRequest;
import com.mcn.in4.domain.team.dto.response.MyTeamResponse;
import com.mcn.in4.domain.team.dto.response.TeamDetailResponse;
import com.mcn.in4.domain.team.dto.response.TeamResponse;
import com.mcn.in4.domain.team.entity.Team;
import com.mcn.in4.domain.team.entity.TeamRelay;
import com.mcn.in4.domain.team.repository.TeamRelayRepository;
import com.mcn.in4.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamRelayRepository teamRelayRepository;
    private final MemberRepository memberRepository;

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

    @Override
    @Transactional
    public void createTeam(TeamCreateRequest request) {
        // 1. 팀 저장 및 즉시 반영
        Team team = new Team(null, request.getTeamName(), request.getTeamDetail());
        Team savedTeam = teamRepository.saveAndFlush(team);

        // 2. 멤버 조회
        List<Member> members = memberRepository.findAllById(request.getMemberIds());

        if (members.isEmpty()) return;

        // 3. TeamRelay 생성
        List<TeamRelay> relays = members.stream()
                .map(member -> {
                    // 수동 ID 부여 대신 builder나 생성자를 통해
                    // ID를 제외한 정보만 세팅 (엔티티에 @GeneratedValue가 있을 경우)
                    return new TeamRelay(null, savedTeam, member);
                })
                .toList();

        // 4. 저장 및 강제 반영
        teamRelayRepository.saveAllAndFlush(relays);
    }

    @Override
    @Transactional
    public void updateTeamMembers(Long teamId, TeamMemberUpdateRequest request) {
        // 1. 대상 팀 존재 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("해당 팀을 찾을 수 없습니다."));

        // 2. 기존 팀 멤버 연결(Relay) 전체 삭제
        teamRelayRepository.deleteAllByTeamId(teamId);

        // 3. 새로 요청된 멤버들 조회
        List<Member> newMembers = memberRepository.findAllById(request.getMemberIds());

        if (newMembers.isEmpty()) {
            return; // 혹은 모든 멤버를 제거하는 것이 의도라면 여기서 종료
        }

        // 4. 새로운 연결 데이터(Relay) 생성 및 저장
        List<TeamRelay> newRelays = newMembers.stream()
                .map(member -> new TeamRelay(null, team, member)) // ID는 자동생성(IDENTITY) 가정
                .toList();

        teamRelayRepository.saveAll(newRelays);
    }

    @Override
    @Transactional
    public void deleteTeam(Long teamId) {
        // 1. 삭제할 팀이 존재하는지 먼저 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("삭제하려는 팀이 존재하지 않습니다. ID: " + teamId));

        // 2. 해당 팀과 연결된 멤버 관계(TeamRelay)를 먼저 삭제 (FK 제약 조건 해결)
        teamRelayRepository.deleteAllByTeamId(teamId);

        // 3. 팀 엔티티 삭제
        teamRepository.delete(team);
    }

    // TeamServiceImpl.java

    @Override
    @Transactional(readOnly = true)
    public List<MyTeamResponse> getMyTeams(String tokenMemberId, Long requestedMemberId) {
        // 1. 보안 검증 (토큰 주인 확인)
        if (!tokenMemberId.equals(String.valueOf(requestedMemberId))) {
            throw new IllegalArgumentException("본인의 정보만 조회할 수 있습니다.");
        }

        // 2. 내가 속한 팀 연결 정보 조회
        List<TeamRelay> myRelays = teamRelayRepository.findAllByMemberMemberId(requestedMemberId);

        if (myRelays.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. 데이터 변환
        return myRelays.stream().map(relay -> {
            Team team = relay.getTeam();

            // [수정된 부분] 이미 있는 메서드 활용 (팀원 목록 조회)
            List<TeamRelay> allTeamRelays = teamRelayRepository.findAllByTeamIdWithMemberAndDept(team.getTeamId());

            List<MyTeamResponse.TeamMemberResponse> memberResponses = allTeamRelays.stream()
                    .map(tr -> {
                        var m = tr.getMember();
                        String deptName = (m.getDepartment() != null) ? m.getDepartment().getDepartmentName() : "무소속";
                        return new MyTeamResponse.TeamMemberResponse(
                                m.getMemberId(),
                                m.getMemberName(),
                                deptName,
                                m.getTask()
                        );
                    }).toList();

            // [중요] 빌더에 team의 실제 데이터를 확실히 바인딩
            return MyTeamResponse.builder()
                    .teamId(team.getTeamId())
                    .teamName(team.getTeamName())
                    .teamDetail(team.getTeamDetail())
                    .teamMembers(memberResponses)
                    .build();
        }).toList();
    }
}