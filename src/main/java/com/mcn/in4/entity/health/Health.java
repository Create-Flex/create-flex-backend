package com.mcn.in4.entity.health;

import com.mcn.in4.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "health") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Health {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_id")
    private Long healthId;
    //건강 키

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    //검진자 키

    @Column(name = "checkup_name", nullable = false)
    private String checkupName;
    //검진명

    @Column(name = "checkup_date", nullable = false)
    private LocalDate checkupDate;
    //검진일

    @Enumerated(EnumType.STRING)
    @Column(name = "checkup_summanary", nullable = false)
    private CheckupSummanary checkupSummanary;
    //검진 종합 상태

    @Column(name = "checkup_file_url", nullable = false)
    private String checkupFileUrl;
    //제출된 검진지 URL
}
