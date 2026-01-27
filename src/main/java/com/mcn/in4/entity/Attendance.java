package com.mcn.in4.entity;

import com.mcn.in4.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long AttendanceId;
    //근태 키

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    //근무자 키

    @Column(name = "attendance_date", nullable=false)
    private LocalDate attendanceDate;
    //근무일

    @Column(name = "attendance_start", nullable = false)
    private LocalDateTime attendanceStart;
    //근무 시작시간

    @Column(name = "attendance_end", nullable=true)
    private LocalDateTime attendanceEnd;
    //근무 종료시간
}
