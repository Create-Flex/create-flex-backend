package com.mcn.in4.domain.attendance.entity.attendanceEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
/**
 * 근태 상태 Enum
 * 근무중, 정상, 지각, 조퇴, 퇴근 등의 상태를 정의합니다.
 */
public enum AttendanceStatus {

    WORKING("근무중"), // 입실 후 퇴실 전
    NORMAL("출근"), // 정상 출근 사용자가 '출근'이라 명명함
    LATE("지각"), // 지각
    EARLY_LEAVE("조퇴"), // 조퇴
    OVERTIME("초과"), // 9시 이전 출근 + 18시 이후 퇴근
    OFF_WORK("퇴근");

    private final String description;
}