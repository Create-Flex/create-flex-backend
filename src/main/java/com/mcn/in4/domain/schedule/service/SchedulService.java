package com.mcn.in4.domain.schedule.service;

import com.mcn.in4.domain.schedule.dto.resquestDTO.ScheduleRequestDTO;

public interface SchedulService {
    void createSchedule(Long memberId, String role, ScheduleRequestDTO.ScheduleCreateRequestDto requestDto);
}
