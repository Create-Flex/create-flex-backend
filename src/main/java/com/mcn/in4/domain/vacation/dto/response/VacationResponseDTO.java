package com.mcn.in4.domain.vacation.dto.response;

import com.mcn.in4.domain.vacation.entity.Vacation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

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
