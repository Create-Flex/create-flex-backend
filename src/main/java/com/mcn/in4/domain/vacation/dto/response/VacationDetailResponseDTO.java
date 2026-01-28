package com.mcn.in4.domain.vacation.dto.response;

import com.mcn.in4.domain.vacation.entity.Vacation;
import com.mcn.in4.domain.vacation.entity.VacationFamily;
import com.mcn.in4.domain.vacation.entity.VacationSick;
import com.mcn.in4.domain.vacation.entity.VacationWorkation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 휴가 상세 조회 응답 DTO
 * - 휴가 유형별 상세 정보를 포함 (경조사, 병가, 워케이션)
 * - 모달 팝업에서 사용
 */
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

    /** 기본 휴가 정보를 DTO로 변환 (연차, 반차용) */
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

    /** 경조사 휴가 정보를 DTO로 변환 (경조사 상세 정보 포함) */
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

    /** 병가 휴가 정보를 DTO로 변환 (병가 상세 정보 포함) */
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

    /** 워케이션 휴가 정보를 DTO로 변환 (워케이션 상세 정보 포함) */
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
