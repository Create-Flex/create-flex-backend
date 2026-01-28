package com.mcn.in4.domain.attendance.service;

import com.mcn.in4.domain.attendance.dto.AttendanceResponseDto;

import java.util.List;

public interface AttendanceService {
    List<AttendanceResponseDto> getAttendanceByMemberId(Long memberId);
    void checkIn(Long memberId);
    void checkOut(Long memberId);
}