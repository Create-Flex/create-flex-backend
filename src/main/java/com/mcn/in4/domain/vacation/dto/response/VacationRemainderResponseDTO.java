package com.mcn.in4.domain.vacation.dto.response;

import com.mcn.in4.domain.member.entity.MemberEmployeeDetail;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VacationRemainderResponseDTO {
    private Long memberId;
    private String memberName;
    private Double totalVacation;       // 총 연차 (15일)
    private Double usedVacation;        // 사용 연차
    private Double remainderVacation;   // 잔여 연차

    public static VacationRemainderResponseDTO from(MemberEmployeeDetail employeeDetail, double totalVacation) {
        double remainder = employeeDetail.getVacationRemainder();
        double used = totalVacation - remainder;

        return VacationRemainderResponseDTO.builder()
                .memberId(employeeDetail.getMember().getMemberId())
                .memberName(employeeDetail.getMember().getMemberName())
                .totalVacation(totalVacation)
                .usedVacation(used)
                .remainderVacation(remainder)
                .build();
    }
}
