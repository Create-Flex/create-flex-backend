package com.mcn.in4.domain.attendance.entity.attendanceEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
/**
 * 퇴근 상태 Enum
 * 퇴근 시점의 상태를 정의합니다.
 */
public enum CheckOutStatus {
    EARLY_LEAVE("조퇴"), // 조퇴 (18:00 이전)
    NORMAL("퇴근"), // 정상 퇴근 (18:00)
    OVERTIME("초과"); // 초과 근무 (18:00 이후)

    private final String description;
}
