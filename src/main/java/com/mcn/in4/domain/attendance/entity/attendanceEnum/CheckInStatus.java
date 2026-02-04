package com.mcn.in4.domain.attendance.entity.attendanceEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
/**
 * 출근 상태 Enum
 * 출근 시점의 상태를 정의합니다.
 */
public enum CheckInStatus {
    NORMAL("출근"), // 정상 출근 (09:00 이전)
    LATE("지각"), // 지각 (09:00 이후)
    ABSENT("결근"); // 결근 (배치 처리)

    private final String description;
}
