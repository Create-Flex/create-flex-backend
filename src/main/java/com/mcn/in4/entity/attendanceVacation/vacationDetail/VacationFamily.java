package com.mcn.in4.entity.attendanceVacation.vacationDetail;

import com.mcn.in4.entity.attendanceVacation.Vacation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vacation_family") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VacationFamily {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_id")
    private Long familyId;
    //경조사 키

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vacation_id", nullable = false)
    private Vacation vacation;
    //휴가 키

    @Column(name = "family_relation", nullable = false)
    private String familyRelation;
    //경조사 관계

    @Column(name = "family_detail", nullable = false)
    private String familyDetail;
    //경조사 내용
}
