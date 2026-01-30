package com.mcn.in4.domain.schedule.service;

import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.repository.MemberRepository;
import com.mcn.in4.domain.schedule.dto.resquestDTO.ScheduleRequestDTO;
import com.mcn.in4.domain.schedule.entity.Schedule;
import com.mcn.in4.domain.schedule.entity.scheduleEnum.ScheduleType;
import com.mcn.in4.domain.schedule.repository.SchedulRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulServiceImpl implements SchedulService{
    private final SchedulRepository schedulRepository;
    private final MemberRepository memberRepository;

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

}
