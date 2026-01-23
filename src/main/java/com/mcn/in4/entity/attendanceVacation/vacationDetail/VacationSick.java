package com.mcn.in4.entity.attendanceVacation.vacationDetail;

import com.mcn.in4.entity.attendanceVacation.Vacation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vacation_sick") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VacationSick {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sick_id")
    private Long sickId;
    //병가 키

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vacation_id", nullable = false)
    private Vacation vacation;
    //휴가 키

    @Column(name = "sick_detail", nullable = false)
    private String sickDetail;
    //증상 상세
}
