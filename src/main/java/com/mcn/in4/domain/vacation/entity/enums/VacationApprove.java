package com.mcn.in4.domain.vacation.entity.enums;

/**
 * 휴가 승인 상태
 */
public enum VacationApprove {
    APPROVE_NEED("승인 대기"),
    APPROVED("승인됨"),
    REJECTED("반려됨");

    private final String description;

    VacationApprove(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
