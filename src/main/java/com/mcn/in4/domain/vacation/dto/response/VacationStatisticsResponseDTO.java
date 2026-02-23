package com.mcn.in4.domain.vacation.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 휴가 통계 응답 DTO (관리자용)
 * - 이번달 휴가자 수, 미승인 대기자 수, 이번달 병가자 수
 */
@Getter
@Builder
public class VacationStatisticsResponseDTO {

    /** 이번달 휴가자 수 (승인된 휴가) */
    private long monthlyVacationCount;

    /** 총 미승인 대기자 수 */
    private long pendingApprovalCount;

    /** 이번달 병가자 수 (승인된 병가) */
    private long monthlySickLeaveCount;

    /** 통계 기준 연월 (예: "2026-01") */
    private String targetMonth;

    /** 통계 DTO 생성 */
    public static VacationStatisticsResponseDTO of(
            long monthlyVacationCount,
            long pendingApprovalCount,
            long monthlySickLeaveCount,
            String targetMonth) {
        return VacationStatisticsResponseDTO.builder()
                .monthlyVacationCount(monthlyVacationCount)
                .pendingApprovalCount(pendingApprovalCount)
                .monthlySickLeaveCount(monthlySickLeaveCount)
                .targetMonth(targetMonth)
                .build();
    }
}
