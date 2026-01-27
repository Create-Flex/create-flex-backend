package com.mcn.in4.domain.vacation.dto.response;

import com.mcn.in4.domain.vacation.entity.Vacation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class VacationListResponseDTO {
    private Long vacationId;
    private String vacationPeriod;      // "2026-01-05 ~ 2026-01-06"
    private String vacationType;         // ANNUAL, HALF, FAMILY, SICK, WORKATION
    private Double vacationDays;         // 사용일수
    private String vacationDetail;       // 신청사유
    private String vacationApprove;      // 승인상태

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static VacationListResponseDTO from(Vacation vacation) {
        String period = formatPeriod(vacation.getVacationStart(), vacation.getVacationEnd());

        return VacationListResponseDTO.builder()
                .vacationId(vacation.getVacationId())
                .vacationPeriod(period)
                .vacationType(vacation.getVacationType().name())
                .vacationDays(vacation.getVacationDays())
                .vacationDetail(vacation.getVacationDetail())
                .vacationApprove(vacation.getVacationApprove().name())
                .build();
    }

    private static String formatPeriod(LocalDate start, LocalDate end) {
        if (start.equals(end)) {
            return start.format(DATE_FORMATTER);
        }
        return start.format(DATE_FORMATTER) + " ~ " + end.format(DATE_FORMATTER);
    }
}
