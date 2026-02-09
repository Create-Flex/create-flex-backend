package com.mcn.in4.domain.attendance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
/**
 * 대시보드 통계 DTO
 * 지각 횟수와 초과 근무 시간(분)을 포함합니다.
 */
public class AttendanceDashboardDto {
    private int lateCount;
    private long totalOvertimeMinutes;
    private long totalWorkMinutes;
}
