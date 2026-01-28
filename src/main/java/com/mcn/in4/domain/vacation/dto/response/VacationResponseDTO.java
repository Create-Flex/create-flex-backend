package com.mcn.in4.domain.vacation.dto.response;

import com.mcn.in4.domain.vacation.entity.Vacation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 휴가 신청 응답 DTO
 * - 휴가 신청 완료 후 반환되는 정보
 */
@Getter
@Builder
public class VacationResponseDTO {
    private Long vacationId;
    private String memberName;
    private String vacationType;
    private LocalDate vacationStart;
    private LocalDate vacationEnd;
    private Double vacationDays;
    private String vacationDetail;
    private String vacationApprove;
    private LocalDate vacationRequest;

    /** Vacation 엔티티를 응답 DTO로 변환 */
    public static VacationResponseDTO from(Vacation vacation) {
        return VacationResponseDTO.builder()
                .vacationId(vacation.getVacationId())
                .memberName(vacation.getMember().getMemberName())
                .vacationType(vacation.getVacationType().name())
                .vacationStart(vacation.getVacationStart())
                .vacationEnd(vacation.getVacationEnd())
                .vacationDays(vacation.getVacationDays())
                .vacationDetail(vacation.getVacationDetail())
                .vacationApprove(vacation.getVacationApprove().name())
                .vacationRequest(vacation.getVacationRequest())
                .build();
    }
}
