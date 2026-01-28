package com.mcn.in4.domain.vacation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 휴가 신청 요청 DTO
 * - 휴가 유형별로 필요한 필드가 다름
 * - 공통 필드: memberId, vacationStart, vacationEnd, vacationDetail
 */
@Getter
@NoArgsConstructor
public class VacationRequestDTO {
    // 공통 필드 (필수)
    private Long memberId;
    private LocalDate vacationStart;
    private LocalDate vacationEnd;
    private String vacationDetail;

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
}
