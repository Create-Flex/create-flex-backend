package com.mcn.in4.domain.attendance.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
/**
 * 근태 응답 DTO
 * 클라이언트에게 반환할 근태 정보를 담는 객체입니다.
 */
public class AttendanceResponseDto {
    private Long memberId;
    private String memberName;
    private Long attendanceId;
    private LocalDate attendanceDate;
    private LocalDateTime attendanceStart;
    private LocalDateTime attendanceEnd;
    private String attendanceStatus;
}