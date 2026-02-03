package com.mcn.in4.domain.team.service;

import com.mcn.in4.domain.attendance.repository.AttendanceRepository;
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
import com.mcn.in4.domain.vacation.entity.enums.VacationApprove;
import com.mcn.in4.domain.vacation.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final AttendanceRepository attendanceRepository;
    private final VacationRepository vacationRepository;

    @Override
    public List<TeamResponse> findAllTeams() {
        return teamRepository.findAll().stream()
                .map(TeamResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public TeamDetailResponse getTeamDetail(Long teamId) {
        //팀 정보 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("해당 팀을 찾을 수 없습니다."));

        //소속 멤버 상세 조회 (Fetch Join 활용)
        List<TeamRelay> relays = teamRelayRepository.findAllByTeamIdWithMemberAndDept(teamId);

        //DTO 변환 및 근태/휴가 상태 확인
        List<TeamDetailResponse.TeamMemberResponse> memberResponses = relays.stream()
                .map(tr -> {
                    var m = tr.getMember();
                    String deptName = (m.getDepartment() != null) ? m.getDepartment().getDepartmentName() : "무소속";

                    // 근무 상태 확인 로직 시작
                    String workStatus = "미출근";
                    LocalDate today = LocalDate.now();

                    // 1. 휴가 여부 확인 (VacationRepository 활용)
                    boolean isVacation = vacationRepository.isMemberOnVacation(m.getMemberId(), today, VacationApprove.APPROVED);

                    if (isVacation) {
                        workStatus = "휴가";
                    } else {
                        // 2. 출근 여부 확인 (AttendanceRepository 활용)
                        var attendanceOpt = attendanceRepository.findByMemberIdAndAttendanceDate(m.getMemberId(), today);
                        if (attendanceOpt.isPresent()) {
                            // 출근 기록이 있으면 해당 상태(근무중, 퇴근, 지각 등)의 설명을 가져옴
                            workStatus = attendanceOpt.get().getAttendanceStatus().getDescription();
                        }
                    }

                    return new TeamDetailResponse.TeamMemberResponse(
                            m.getMemberId(), m.getMemberName(), deptName, m.getTask(), workStatus
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
        //팀 저장 및 즉시 반영
        Team team = new Team(null, request.getTeamName(), request.getTeamDetail());
        Team savedTeam = teamRepository.saveAndFlush(team);

        //멤버 조회
        List<Member> members = memberRepository.findAllById(request.getMemberIds());

        if (members.isEmpty()) return;

        //TeamRelay 생성
        List<TeamRelay> relays = members.stream()
                .map(member -> {
                    // 수동 ID 부여 대신 builder나 생성자를 통해
                    // ID를 제외한 정보만 세팅 (엔티티에 @GeneratedValue가 있을 경우)
                    return new TeamRelay(null, savedTeam, member);
                })
                .toList();

        //저장 및 강제 반영
        teamRelayRepository.saveAllAndFlush(relays);
    }

    @Override
    @Transactional
    public void updateTeamMembers(Long teamId, TeamMemberUpdateRequest request) {
        //대상 팀 존재 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("해당 팀을 찾을 수 없습니다."));

        //기존 팀 멤버 연결(Relay) 전체 삭제
        teamRelayRepository.deleteAllByTeamId(teamId);

        //새로 요청된 멤버들 조회
        List<Member> newMembers = memberRepository.findAllById(request.getMemberIds());

        if (newMembers.isEmpty()) {
            return; // 혹은 모든 멤버를 제거하는 것이 의도라면 여기서 종료
        }

        //새로운 연결 데이터(Relay) 생성 및 저장
        List<TeamRelay> newRelays = newMembers.stream()
                .map(member -> new TeamRelay(null, team, member)) // ID는 자동생성(IDENTITY) 가정
                .toList();

        teamRelayRepository.saveAll(newRelays);
    }

    @Override
    @Transactional
    public void deleteTeam(Long teamId) {
        //삭제할 팀이 존재하는지 먼저 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("삭제하려는 팀이 존재하지 않습니다. ID: " + teamId));

        //해당 팀과 연결된 멤버 관계(TeamRelay)를 먼저 삭제 (FK 제약 조건 해결)
        teamRelayRepository.deleteAllByTeamId(teamId);

        //팀 엔티티 삭제
        teamRepository.delete(team);
    }

    // TeamServiceImpl.java

    @Override
    public List<MyTeamResponse> getMyTeams(String memberIdStr) {
        Long memberId = Long.parseLong(memberIdStr);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<TeamRelay> myRelays = teamRelayRepository.findAllByMemberMemberId(member.getMemberId());

        if (myRelays.isEmpty()) {
            return Collections.emptyList();
        }

        return myRelays.stream().map(relay -> {
            Team team = relay.getTeam();
            List<TeamRelay> teamMembers = teamRelayRepository.findAllByTeamIdWithMemberAndDept(team.getTeamId());

            List<MyTeamResponse.TeamMemberResponse> memberResponses = teamMembers.stream()
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

            return MyTeamResponse.builder()
                    .teamId(team.getTeamId())
                    .teamName(team.getTeamName())
                    .teamDetail(team.getTeamDetail())
                    .teamMembers(memberResponses)
                    .build();
        }).toList();
    }

    @Override
    public MyTeamResponse getMyTeamDetail(String memberIdStr, Long teamId) {
        Long memberId = Long.parseLong(memberIdStr);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 보안 검증: 내 ID로 검색
        boolean isMyTeam = teamRelayRepository.findAllByMemberMemberId(member.getMemberId())
                .stream()
                .anyMatch(relay -> relay.getTeam().getTeamId().equals(teamId));

        if (!isMyTeam) {
            throw new IllegalArgumentException("해당 팀의 정보를 조회할 권한이 없습니다.");
        }

        List<TeamRelay> teamRelays = teamRelayRepository.findAllByTeamIdWithMemberAndDept(teamId);

        if (teamRelays.isEmpty()) {
            throw new IllegalArgumentException("팀 정보를 찾을 수 없습니다.");
        }

        Team team = teamRelays.get(0).getTeam();

        List<MyTeamResponse.TeamMemberResponse> memberResponses = teamRelays.stream()
                .map(tr -> {
                    Member m = tr.getMember();
                    String deptName = (m.getDepartment() != null) ? m.getDepartment().getDepartmentName() : "무소속";
                    return new MyTeamResponse.TeamMemberResponse(
                            m.getMemberId(),
                            m.getMemberName(),
                            deptName,
                            m.getTask()
                    );
                }).toList();

        return MyTeamResponse.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .teamDetail(team.getTeamDetail())
                .teamMembers(memberResponses)
                .build();
    }
}