package com.mcn.in4.domain.vacation.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 병가 휴가 상세 엔티티
 * - 휴가 유형이 SICK인 경우의 추가 정보를 저장
 */
@Entity
@Table(name = "vacation_sick")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VacationSick {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sick_id")
    private Long sickId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vacation_id", nullable = false)
    private Vacation vacation;

    @Column(name = "sick_detail", nullable = false)
    private String sickDetail;      // 증상 및 사유

    @Column(name = "sick_hospital", nullable = false)
    private String sickHospital;    // 진료 예정 병원
}
