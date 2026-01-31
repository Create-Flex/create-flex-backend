package com.mcn.in4.domain.schedule.service;

import com.mcn.in4.domain.schedule.dto.responseDTO.SchedulReponseDTO;
import com.mcn.in4.domain.schedule.dto.resquestDTO.ScheduleRequestDTO;

import java.util.List;

public interface SchedulService {
    void createSchedule(Long memberId, String role, ScheduleRequestDTO.ScheduleCreateRequestDto requestDto);
    List<SchedulReponseDTO.ScheduleResponseDto> getMyMonthlySchedules(Long memberId, String month);


}
