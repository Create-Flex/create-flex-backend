package com.mcn.in4.entity.member;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_profile") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;
    //프로필 키

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(
            foreignKeyDefinition =
                    "FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE"
    )) //상위 엔티티 삭제시 이 엔티티도 삭제
    private Member member;
    //사용자 키

    @Column(name = "profile_image", nullable = false)
    private String profileImage;
    //프로필 사진

    @Column(name = "profile_banner", nullable = false)
    private String profileBanner;
    //배너 사진
}
