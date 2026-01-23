package com.mcn.in4.entity.attendanceVacation;

import com.mcn.in4.entity.attendanceVacation.vacationEnum.VacationApprove;
import com.mcn.in4.entity.attendanceVacation.vacationEnum.VacationType;
import com.mcn.in4.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "vacation") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Vacation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vacation_id")
    private Long vacationId;
    //휴가 키

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    //휴가 신청자

    @Enumerated(EnumType.STRING)
    @Column(name = "vacation_type", nullable = false)
    private VacationType vacationType;
    //휴가 타입

    @Column(name = "vacation_start", nullable = false)
    private LocalDate vacationStart;
    //휴가 시작일

    @Column(name = "vacation_end", nullable = false)
    private LocalDate vacationEnd;
    //휴가 종료일

    @Column(name = "vacation_request", nullable = false)
    private LocalDate vacationRequest;
    //휴가 신청일

    @Column(name = "vacation_detail", nullable=true)
    private String vacationDetail;
    //휴가 상세 사유

    @Enumerated(EnumType.STRING)
    @Column(name = "vacation_approve", nullable = false)
    private VacationApprove vacationApprove;
    //승인 여부

    @Column(name = "vacation_rejected", nullable = true)
    private String vacationRejected;
    //반려사유
}
