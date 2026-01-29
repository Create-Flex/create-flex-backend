package com.mcn.in4.domain.attendance.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class AttendanceResponseDto {
    private Long memberId;
    private String memberName;
    private Long attendanceId;
    private LocalDate attendanceDate;
    private LocalDateTime attendanceStart;
    private LocalDateTime attendanceEnd;
    private String attendanceStatus;
}