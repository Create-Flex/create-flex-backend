package com.mcn.in4.domain.vacation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 휴가 반려 요청 DTO (관리자용)
 */
@Getter
@NoArgsConstructor
public class VacationRejectRequestDTO {
    private String rejectReason;    // 반려 사유
}
