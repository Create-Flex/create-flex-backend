package com.mcn.in4.entity.schedule;

import com.mcn.in4.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "schedule_visitor") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ScheduleVisitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_visitor_id")
    private Long scheduleVisitorId;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false, foreignKey = @ForeignKey(
            foreignKeyDefinition =
                    "FOREIGN KEY (schedule_id) REFERENCES schedule(id) ON DELETE CASCADE"
    ))
    private Schedule schedule;
    //참가자 키

    @ManyToOne
    @JoinColumn(name = "visitor_id", nullable = false, foreignKey = @ForeignKey(
            foreignKeyDefinition =
                    "FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE"
    ))
    private Member member;
    //참가자 키
}
