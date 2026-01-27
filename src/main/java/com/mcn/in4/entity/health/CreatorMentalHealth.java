package com.mcn.in4.entity.health;

import com.mcn.in4.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "creator_metal_health") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreatorMentalHealth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creator_mental_id")
    private Long creatorMentalId;
    //정신건강 키

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    //정신 설문자 키

    @Column(name = "creator_mental_date", nullable = false)
    private LocalDate creatorMentalDate;
    //정신 설문 날짜

    @Column(name = "creator_mental_score", nullable = false)
    private Long creatorMentalScore;
    //정신 설문 점수
}
