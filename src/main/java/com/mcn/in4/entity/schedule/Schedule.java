package com.mcn.in4.entity.schedule;

import com.mcn.in4.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "schedule") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;
    //스케쥴 ID

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    //일정 제작자 키

    @Column(name = "schedule_name", nullable = false)
    private String scheduleName;
    //일정 제목

    @Column(name = "schedule_date", nullable = false)
    private LocalDate scheduleDate;
    //일정 날짜

    @Column(name = "schedule_detail", nullable = true)
    private String scheduleDetail;
    //일정 상세

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type", nullable = false)
    private ScheduleType scheduleType;
    //일정 종류
}
