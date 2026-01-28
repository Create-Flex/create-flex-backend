package com.mcn.in4.domain.vacation.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 워케이션 휴가 상세 엔티티
 * - 휴가 유형이 WORKATION인 경우의 추가 정보를 저장
 */
@Entity
@Table(name = "vacation_workation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VacationWorkation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workation_id")
    private Long workationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vacation_id", nullable = false)
    private Vacation vacation;

    @Column(name = "workation_where", nullable = false)
    private String workationWhere;      // 근무 장소

    @Column(name = "workation_contact", nullable = false)
    private String workationContact;    // 비상 연락망

    @Column(name = "workation_plan", nullable = false)
    private String workationPlan;       // 업무 계획 및 목표

    @Column(name = "workation_handover", nullable = false)
    private String workationHandover;   // 업무 인계 사항
}
