package com.mcn.in4.domain.creator.entity;

import com.mcn.in4.domain.creator.entity.creatorEnum.PromotionStatus;
import com.mcn.in4.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "creator_promotion") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CreatorPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_id")
    private Long promotionId;
    //광고계약 키

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_creator_id", nullable = false)
    private Member memberCreator;
    //크리에이터의 사용자 키

    @Column(name = "promotion_client", nullable = false)
    private String promotionClient;
    //광고주명

    @Column(name = "promotion_name", nullable = false)
    private String promotionName;
    //광고 캠페인명

    @Column(name = "promotion_fee", nullable = false)
    private String promotionFee;
    //광고 제안단가

    @Column(name = "promotion_detail", nullable = false)
    private String promotionDetail;
    //광고 상세내용

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "promotion_targer_date", nullable = false)
    private LocalDate promotionTargerDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "promotion_status", nullable = false)
    private PromotionStatus promotionStatus;
}
