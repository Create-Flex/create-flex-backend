package com.mcn.in4.domain.member.entity;

import com.mcn.in4.domain.member.entity.memberEnum.CreatorPlatform;
import com.mcn.in4.domain.member.entity.memberEnum.CreatorStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "creator_detail") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberCreatorDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creator_detail_id")
    private Long creatorDetailId;
    //크리에이터 상세 키

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_creator_id", nullable = false)
    private Member memberCreator;
    //크리에이터의 사용자 키

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_manager_id", nullable = false)
    private Member memberManager;
    //매니저의 사용자 키

    @Column(name = "creator_subscribe", nullable = false)
    private String creatorSubscribe;
    //구독자 수

    @Column(name = "creator_category", nullable = false)
    private String creatorCategory;
    //크리에이터 카테고리

    @Enumerated(EnumType.STRING)
    @Column(name = "creator_platform", nullable = false)
    private CreatorPlatform creatorPlatform;
    //크리에이터 플랫폼

    @Enumerated(EnumType.STRING)
    @Column(name = "creator_status", nullable = false)
    private CreatorStatus creatorStatus;
    //크리에이터 상태
    
    @Column(name = "creator_main_contact", nullable = true)
    private String creatorMainContact;
    //크리에이터 기본 연락망
}