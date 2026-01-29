package com.mcn.in4.domain.attendance.service;

import com.mcn.in4.domain.attendance.dto.AttendanceResponseDto;

import java.util.List;

public interface AttendanceService {
    List<AttendanceResponseDto> getAttendance(Long memberId, java.time.LocalDate startDate, java.time.LocalDate endDate,
            String status);

    List<AttendanceResponseDto> getAllAttendance(java.time.LocalDate startDate, java.time.LocalDate endDate,
            String status);

    void checkIn(Long memberId);

    void checkOut(Long memberId);
}