package com.mcn.in4.entity.attendanceVacation.vacationDetail;

import com.mcn.in4.entity.attendanceVacation.Vacation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vacation_workation") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VacationWorkation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workation_id")
    private Long workationId;
    //워케이션 키

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vacation_id", nullable = false)
    private Vacation vacation;
    //휴가 키

    @Column(name = "workation_where", nullable = false)
    private String workationWhere;
    //근무장소

    @Column(name = "workation_contact", nullable = false)
    private String workationContact;
    //비상연락망

    @Column(name = "workation_plan", nullable = false)
    private String workationPlan;
    //업무계획
}
