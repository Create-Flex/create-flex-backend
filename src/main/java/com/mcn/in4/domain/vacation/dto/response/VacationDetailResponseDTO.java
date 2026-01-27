package com.mcn.in4.domain.vacation.dto.response;

import com.mcn.in4.domain.vacation.entity.Vacation;
import com.mcn.in4.domain.vacation.entity.VacationFamily;
import com.mcn.in4.domain.vacation.entity.VacationSick;
import com.mcn.in4.domain.vacation.entity.VacationWorkation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class VacationDetailResponseDTO {
    // 공통 정보
    private Long vacationId;
    private String memberName;
    private String vacationType;
    private LocalDate vacationStart;
    private LocalDate vacationEnd;
    private Double vacationDays;
    private String vacationDetail;
    private String vacationApprove;
    private LocalDate vacationRequest;
    private String vacationRejected;    // 반려 사유

    // 경조사 (FAMILY)
    private String familyRelation;
    private String familyDetail;

    // 병가 (SICK)
    private String sickDetail;
    private String sickHospital;

    // 워케이션 (WORKATION)
    private String workationWhere;
    private String workationContact;
    private String workationPlan;
    private String workationHandover;

    public static VacationDetailResponseDTO from(Vacation vacation) {
        return VacationDetailResponseDTO.builder()
                .vacationId(vacation.getVacationId())
                .memberName(vacation.getMember().getMemberName())
                .vacationType(vacation.getVacationType().name())
                .vacationStart(vacation.getVacationStart())
                .vacationEnd(vacation.getVacationEnd())
                .vacationDays(vacation.getVacationDays())
                .vacationDetail(vacation.getVacationDetail())
                .vacationApprove(vacation.getVacationApprove().name())
                .vacationRequest(vacation.getVacationRequest())
                .vacationRejected(vacation.getVacationRejected())
                .build();
    }

    public static VacationDetailResponseDTO from(Vacation vacation, VacationFamily family) {
        return VacationDetailResponseDTO.builder()
                .vacationId(vacation.getVacationId())
                .memberName(vacation.getMember().getMemberName())
                .vacationType(vacation.getVacationType().name())
                .vacationStart(vacation.getVacationStart())
                .vacationEnd(vacation.getVacationEnd())
                .vacationDays(vacation.getVacationDays())
                .vacationDetail(vacation.getVacationDetail())
                .vacationApprove(vacation.getVacationApprove().name())
                .vacationRequest(vacation.getVacationRequest())
                .vacationRejected(vacation.getVacationRejected())
                .familyRelation(family.getFamilyRelation())
                .familyDetail(family.getFamilyDetail())
                .build();
    }

    public static VacationDetailResponseDTO from(Vacation vacation, VacationSick sick) {
        return VacationDetailResponseDTO.builder()
                .vacationId(vacation.getVacationId())
                .memberName(vacation.getMember().getMemberName())
                .vacationType(vacation.getVacationType().name())
                .vacationStart(vacation.getVacationStart())
                .vacationEnd(vacation.getVacationEnd())
                .vacationDays(vacation.getVacationDays())
                .vacationDetail(vacation.getVacationDetail())
                .vacationApprove(vacation.getVacationApprove().name())
                .vacationRequest(vacation.getVacationRequest())
                .vacationRejected(vacation.getVacationRejected())
                .sickDetail(sick.getSickDetail())
                .sickHospital(sick.getSickHospital())
                .build();
    }

    public static VacationDetailResponseDTO from(Vacation vacation, VacationWorkation workation) {
        return VacationDetailResponseDTO.builder()
                .vacationId(vacation.getVacationId())
                .memberName(vacation.getMember().getMemberName())
                .vacationType(vacation.getVacationType().name())
                .vacationStart(vacation.getVacationStart())
                .vacationEnd(vacation.getVacationEnd())
                .vacationDays(vacation.getVacationDays())
                .vacationDetail(vacation.getVacationDetail())
                .vacationApprove(vacation.getVacationApprove().name())
                .vacationRequest(vacation.getVacationRequest())
                .vacationRejected(vacation.getVacationRejected())
                .workationWhere(workation.getWorkationWhere())
                .workationContact(workation.getWorkationContact())
                .workationPlan(workation.getWorkationPlan())
                .workationHandover(workation.getWorkationHandover())
                .build();
    }
}
