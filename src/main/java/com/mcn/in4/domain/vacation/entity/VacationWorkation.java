package com.mcn.in4.domain.vacation.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private String workationWhere;

    @Column(name = "workation_contact", nullable = false)
    private String workationContact;

    @Column(name = "workation_plan", nullable = false)
    private String workationPlan;

    @Column(name = "workation_handover", nullable = false)
    private String workationHandover;
}
