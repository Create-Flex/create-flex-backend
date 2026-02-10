package com.mcn.in4.domain.vacation.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 내 휴가 통계 응답 DTO
 * - 승인된 휴가 수 (사용한 휴가)
 * - 미승인 휴가 수 (대기 중인 휴가)
 */
@Getter
@Builder
public class MyVacationStatsResponseDTO {
    private Long memberId;
    private long approvedCount;     // 승인된 휴가 수 (사용한 휴가)
    private long pendingCount;      // 미승인 휴가 수 (대기 중인 휴가)

    public static MyVacationStatsResponseDTO of(Long memberId, long approvedCount, long pendingCount) {
        return MyVacationStatsResponseDTO.builder()
                .memberId(memberId)
                .approvedCount(approvedCount)
                .pendingCount(pendingCount)
                .build();
    }
}
