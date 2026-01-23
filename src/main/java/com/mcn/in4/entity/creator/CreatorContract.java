package com.mcn.in4.entity.creator;

import com.mcn.in4.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "creator_contract") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreatorContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creator_contract_id")
    private Long creatorContractId;
    //크리에이터 계약 키

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_creator_id", nullable = false)
    private Member memberCreator;
    //크리에이터의 사용자 키

    @Column(name = "contract_name", nullable = false)
    private String contractName;
    //계약명

    @Column(name = "contract_start", nullable = false)
    private LocalDate contractStart;
    //계약 시작일

    @Column(name = "contract_end", nullable = false)
    private LocalDate contractEnd;
    //계약 종료일
}
