package com.mcn.in4.domain.vacation.entity.enums;

/**
 * 휴가 유형
 * - ANNUAL, HALF: 잔여 연차에서 차감됨
 * - FAMILY, SICK, WORKATION: 잔여 연차 차감 없음
 */
public enum VacationType {
    ANNUAL,    // 연차
    HALF,      // 반차
    WORKATION, // 워케이션
    FAMILY,    // 경조사
    SICK       // 병가
}
