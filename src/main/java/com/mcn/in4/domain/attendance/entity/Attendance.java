package com.mcn.in4.domain.attendance.entity;

import com.mcn.in4.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.mcn.in4.domain.attendance.entity.attendanceEnum.AttendanceStatus;

@Entity
@Table(name = "attendance") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
/**
 * 근태 엔티티
 * 직원의 출퇴근 기록 정보를 저장하는 엔티티입니다.
 */
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long attendanceId;
    // 근태 키

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    // 근무자 키

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;
    // 근무일

    @Column(name = "attendance_start", nullable = false)
    private LocalDateTime attendanceStart;
    // 근무 시작시간

    @Column(name = "attendance_end", nullable = true)
    private LocalDateTime attendanceEnd;
    // 근무 종료시간

    @Enumerated(EnumType.STRING) // DB에 "WORKING", "LATE" 같은 문자열로 저장됨
    private AttendanceStatus attendanceStatus;

    @Builder
    public Attendance(Member member, LocalDate attendanceDate, LocalDateTime attendanceStart,
            LocalDateTime attendanceEnd, AttendanceStatus attendanceStatus) {
        this.member = member;
        this.attendanceDate = attendanceDate;
        this.attendanceStart = attendanceStart;
        this.attendanceEnd = attendanceEnd;
        this.attendanceStatus = attendanceStatus;
    }

    /**
     * 퇴근 처리 및 상태 업데이트
     */
    public void completeWork(LocalDateTime endTime, AttendanceStatus attendanceStatus) {
        this.attendanceEnd = endTime;
        this.attendanceStatus = attendanceStatus;
    }
}
