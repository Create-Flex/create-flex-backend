package com.mcn.in4.domain.schedule.service;

import com.mcn.in4.domain.creator.repository.CreatorDetailRepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.repository.MemberRepository;
import com.mcn.in4.domain.schedule.dto.responseDTO.SchedulReponseDTO;
import com.mcn.in4.domain.schedule.dto.resquestDTO.ScheduleRequestDTO;
import com.mcn.in4.domain.schedule.entity.Schedule;
import com.mcn.in4.domain.schedule.entity.scheduleEnum.ScheduleType;
import com.mcn.in4.domain.schedule.repository.SchedulRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulServiceImpl implements SchedulService{
    private final SchedulRepository schedulRepository;
    private final MemberRepository memberRepository;
    private final CreatorDetailRepository creatorDetailRepository;


    @Override
    @Transactional
    public void createSchedule(Long memberId, String role, ScheduleRequestDTO.ScheduleCreateRequestDto requestDto) {

        // 직원과 크리에이터 회사일정 등록 불가
        if (("ROLE_EMPLOYEE".equals(role) || "ROLE_CREATOR".equals(role)) &&
                requestDto.getScheduleType() == ScheduleType.COMPANY) {
            throw new IllegalStateException("일반 직원 및 크리에이터는 회사 일정을 등록할 수 있는 권한이 없습니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));


        Schedule schedule = Schedule.builder()
                .member(member)
                .scheduleName(requestDto.getScheduleName())
                .scheduleDate(requestDto.getScheduleDate())
                .scheduleDetail(requestDto.getScheduleDetail())
                .scheduleType(requestDto.getScheduleType())
                .build();
        schedulRepository.save(schedule);
    }
    @Override
    @Transactional(readOnly = true)
    public List<SchedulReponseDTO.ScheduleResponseDto> getMyMonthlySchedules(Long memberId, String month) {
        // month: "YYYY-MM" 년-월
        String[] parts = month.split("-");
        int year = Integer.parseInt(parts[0]);
        int monthValue = Integer.parseInt(parts[1]);

        // 한달
        LocalDate startDate = LocalDate.of(year, monthValue, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());


        // COMPANY(회사, PERSONAL(개인일정) 타입만 필터링
        List<ScheduleType> targetTypes = List.of(ScheduleType.COMPANY, ScheduleType.PERSONAL);
        List<Schedule> schedules = schedulRepository.findMyMonthlySchedules(
                memberId, startDate, endDate, targetTypes);

        return schedules.stream()
                .map(s -> SchedulReponseDTO.ScheduleResponseDto.builder()
                        .scheduleId(s.getScheduleId())
                        .scheduleName(s.getScheduleName())
                        .scheduleDate(s.getScheduleDate())
                        .scheduleDetail(s.getScheduleDetail())
                        .scheduleType(s.getScheduleType())
                        .build())
                .toList();

    }


    //어드민 권한 체크 후 일정 삭제
    @Override
    public void deleteSchedule(Long memberId, String role, Long scheduleId) {

        Schedule schedule = schedulRepository.findById(scheduleId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 일정입니다."));

        boolean isAdmin = "ROLE_ADMINSTRATOR".equals(role)&& schedule.getScheduleType() == ScheduleType.COMPANY;

        boolean isOwner = schedule.getMember().getMemberId().equals(memberId);

        if (!isAdmin && !isOwner) {
            throw new IllegalStateException("삭제 권한이 없습니다. (본인의 일정 또는 관리자의 회사 일정 삭제만 가능)");
        }

        schedulRepository.delete(schedule);
        log.info("알림 삭제 완료");


    }


    //일정 수정 로직, 권한 체크, 작성자 체크
    @Override
    public void updateSchedule(Long memberId, String role, Long scheduleId, ScheduleRequestDTO.ScheduleUpdateRequestDto requestDto) {
        Schedule schedule = schedulRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정이 없습니다."));
        boolean isAdminManager = "ROLE_ADMINISTRATOR".equals(role) && schedule.getScheduleType() == ScheduleType.COMPANY;
        boolean isOwner = schedule.getMember().getMemberId().equals(memberId);
        if (!isAdminManager && !isOwner) {
            throw new IllegalStateException("수정 권한이 없습니다.");
        }
        //엔티티안의 메서드로 수정
        schedule.update(
                requestDto.getScheduleName(),
                requestDto.getScheduleDate(),
                requestDto.getScheduleDetail(),
                requestDto.getScheduleType()
        );

        schedulRepository.save(schedule);
        log.info("알림 수정 완료");
    }

    //크리에이터 일정 조회
    @Override
    public List<SchedulReponseDTO.ScheduleResponseDto> getCreatorSchedules(Long memberId, String role, String month) {
        String[] parts = month.split("-");
        LocalDate startDate = LocalDate.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<ScheduleType> creatorTypes = List.of(ScheduleType.CONTENT,
                ScheduleType.LIVE,
                ScheduleType.MEETING,
                ScheduleType.MERGE,
                ScheduleType.ETC);

        //매니저인 경우 크리에이터 목록 조회
        List<Long> managedCreatorIds = List.of();
        if ("ROLE_MANAGER".equals(role)) {
            managedCreatorIds = creatorDetailRepository.findCreatorIdsByManagerId(memberId);
        }

        List<Schedule> schedules = schedulRepository.findCreatorRelatedSchedules(
                memberId,
                managedCreatorIds.isEmpty() ? List.of(-1L) : managedCreatorIds, // 빈리스트 일때 -1L 을 임의로 넣어 오류 방지
                startDate, endDate, creatorTypes
        );

        return schedules.stream()
                .map(s -> SchedulReponseDTO.ScheduleResponseDto.builder()
                        .scheduleId(s.getScheduleId())
                        .scheduleName(s.getScheduleName())
                        .scheduleDate(s.getScheduleDate())
                        .scheduleDetail(s.getScheduleDetail())
                        .scheduleType(s.getScheduleType())
                        .build())
                .toList();
    }

}
