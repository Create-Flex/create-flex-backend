package com.mcn.in4.domain.vacation.entity;

import com.mcn.in4.domain.vacation.entity.enums.VacationApprove;
import com.mcn.in4.domain.vacation.entity.enums.VacationType;
import com.mcn.in4.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "vacation_type", nullable = false)
    private VacationType vacationType;

    @Column(name = "vacation_start", nullable = false)
    private LocalDate vacationStart;

    @Column(name = "vacation_end", nullable = false)
    private LocalDate vacationEnd;

    @Column(name = "vacation_request", nullable = false)
    private LocalDate vacationRequest;

    @Column(name = "vacation_detail", nullable = true)
    private String vacationDetail;

    @Column(name = "vacation_days", nullable = false)
    private Double vacationDays;
    //휴가 사용 일수 (반차: 0.5, 연차: 시작일~종료일)

    @Enumerated(EnumType.STRING)
    @Column(name = "vacation_approve", nullable = false)
    private VacationApprove vacationApprove;

    @Column(name = "vacation_rejected", nullable = true)
    private String vacationRejected;
}
