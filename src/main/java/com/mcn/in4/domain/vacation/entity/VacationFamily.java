package com.mcn.in4.domain.vacation.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 경조사 휴가 상세 엔티티
 * - 휴가 유형이 FAMILY인 경우의 추가 정보를 저장
 */
@Entity
@Table(name = "vacation_family")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VacationFamily {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_id")
    private Long familyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vacation_id", nullable = false)
    private Vacation vacation;

    @Column(name = "family_relation", nullable = false)
    private String familyRelation;  // 대상(관계) - 예: 조부, 부모 등

    @Column(name = "family_detail", nullable = false)
    private String familyDetail;    // 경조 내용 - 예: 결혼, 장례 등
}
