package com.mcn.in4.domain.attendance.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
/**
 * 전사 근태 대시보드 DTO
 * 전사 평균 출퇴근 시간 및 오늘 근태 현황을 포함합니다.
 */
public class CompanyAttendanceDashboardDto {
    private String averageStartTime; // 평균 출근 시간 (09:00)
    private String averageEndTime; // 평균 퇴근 시간 (18:00)
    private String averageWorkTime; // 평균 근무 시간 (9h 00m)

    private int todayNormalCount; // 오늘 정상 출근 수
    private int todayLateCount; // 오늘 지각 수
    private int todayAbsentCount; // 오늘 결근 수
}
