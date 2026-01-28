package com.mcn.in4.domain.vacation.entity;

import com.mcn.in4.domain.vacation.entity.enums.VacationApprove;
import com.mcn.in4.domain.vacation.entity.enums.VacationType;
import com.mcn.in4.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * 휴가 신청 엔티티
 * - 모든 휴가 유형(연차, 반차, 병가, 경조사, 워케이션)의 공통 정보를 저장
 * - 휴가 유형별 상세 정보는 VacationFamily, VacationSick, VacationWorkation 엔티티에서 관리
 */
@Entity
@Table(name = "vacation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Vacation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vacation_id")
    private Long vacationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;              // 신청자

    @Enumerated(EnumType.STRING)
    @Column(name = "vacation_type", nullable = false)
    private VacationType vacationType;  // 휴가 유형

    @Column(name = "vacation_start", nullable = false)
    private LocalDate vacationStart;    // 휴가 시작일

    @Column(name = "vacation_end", nullable = false)
    private LocalDate vacationEnd;      // 휴가 종료일

    @Column(name = "vacation_request", nullable = false)
    private LocalDate vacationRequest;  // 신청일

    @Column(name = "vacation_detail", nullable = true)
    private String vacationDetail;      // 휴가 사유

    @Column(name = "vacation_days", nullable = false)
    private Double vacationDays;        // 휴가 일수 (반차: 0.5, 연차: 시작일~종료일)

    @Enumerated(EnumType.STRING)
    @Column(name = "vacation_approve", nullable = false)
    private VacationApprove vacationApprove;  // 승인 상태

    @Column(name = "vacation_rejected", nullable = true)
    private String vacationRejected;    // 반려 사유

    /** 휴가 승인 처리 */
    public void approve() {
        this.vacationApprove = VacationApprove.APPROVED;
    }

    /** 휴가 반려 처리 */
    public void reject(String reason) {
        this.vacationApprove = VacationApprove.REJECTED;
        this.vacationRejected = reason;
    }
}
