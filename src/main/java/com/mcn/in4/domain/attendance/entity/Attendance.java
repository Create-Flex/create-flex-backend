package com.mcn.in4.domain.attendance.entity;

import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.attendance.entity.attendanceEnum.CheckInStatus;
import com.mcn.in4.domain.attendance.entity.attendanceEnum.CheckOutStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "attendance_start")
    private LocalDateTime attendanceStart;

    @Column(name = "attendance_end")
    private LocalDateTime attendanceEnd;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_in_status")
    private CheckInStatus checkInStatus; // 출근 상태: NORMAL, LATE, ABSENT

    @Enumerated(EnumType.STRING)
    @Column(name = "check_out_status")
    private CheckOutStatus checkOutStatus; // 퇴근 상태: EARLY_LEAVE, NORMAL, OVERTIME (null = 미퇴근)

    @Builder
    public Attendance(Member member, LocalDate attendanceDate, LocalDateTime attendanceStart,
            LocalDateTime attendanceEnd, CheckInStatus checkInStatus, CheckOutStatus checkOutStatus) {
        this.member = member;
        this.attendanceDate = attendanceDate;
        this.attendanceStart = attendanceStart;
        this.attendanceEnd = attendanceEnd;
        this.checkInStatus = checkInStatus;
        this.checkOutStatus = checkOutStatus;
    }

    /**
     * 퇴근 처리 및 상태 업데이트
     */
    public void completeWork(LocalDateTime endTime, CheckOutStatus checkOutStatus) {
        this.attendanceEnd = endTime;
        this.checkOutStatus = checkOutStatus;
    }
}
