package com.mcn.in4.entity.member;

import com.mcn.in4.entity.member.memberEnum.CreatorPlatform;
import com.mcn.in4.entity.member.memberEnum.CreatorStatus;
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

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false, foreignKey = @ForeignKey(
            foreignKeyDefinition =
                    "FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE"
    )) //상위 엔티티 삭제시 함께 삭제
    private Member memberCreator;
    //크리에이터의 사용자 키

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false, foreignKey = @ForeignKey(
            foreignKeyDefinition =
                    "FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE"
    ))
    private Member memberManager;
    //매니저의 사용자 키

    @Column(name = "creator_subscribe", nullable = false)
    private String creatorSubscribe;
    //구독자 수

    @Column(name = "creator_category", nullable = false)
    private String creatorCategory;
    //크리에이터 카테고리

    @Enumerated(EnumType.STRING)
    @Column(name = "creator_status", nullable = false)
    private CreatorPlatform creatorPlatform;
    //크리에이터 플랫폼

    @Enumerated(EnumType.STRING)
    @Column(name = "creator_status", nullable = false)
    private CreatorStatus creatorStatus;
    //크리에이터 상태
}
