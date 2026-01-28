package com.mcn.in4.domain.vacation.dto.response;

import com.mcn.in4.domain.vacation.entity.Vacation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 관리자용 휴가 목록 조회 응답 DTO
 * - 전체 직원의 휴가 신청 목록을 조회할 때 사용
 * - 필터: 기간, 승인상태, 이름검색
 */
@Getter
@Builder
public class AdminVacationListResponseDTO {
    private Long vacationId;
    private LocalDate vacationRequest;      // 신청일자
    private String memberName;              // 이름
    private String vacationType;            // 휴가유형
    private LocalDate vacationStart;        // 시작일
    private LocalDate vacationEnd;          // 종료일
    private Double vacationDays;            // 휴가일수
    private Double vacationRemainder;       // 잔여연차
    private String vacationApprove;         // 결재상태

    /** Vacation 엔티티와 잔여 연차 정보를 관리자용 목록 DTO로 변환 */
    public static AdminVacationListResponseDTO from(Vacation vacation, Double vacationRemainder) {
        return AdminVacationListResponseDTO.builder()
                .vacationId(vacation.getVacationId())
                .vacationRequest(vacation.getVacationRequest())
                .memberName(vacation.getMember().getMemberName())
                .vacationType(vacation.getVacationType().name())
                .vacationStart(vacation.getVacationStart())
                .vacationEnd(vacation.getVacationEnd())
                .vacationDays(vacation.getVacationDays())
                .vacationRemainder(vacationRemainder)
                .vacationApprove(vacation.getVacationApprove().name())
                .build();
    }
}
