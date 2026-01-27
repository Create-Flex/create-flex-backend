package com.mcn.in4.domain.member.entity;

import com.mcn.in4.domain.department.entity.Department;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.entity.memberEnum.MemberStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "members") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;
    //사용자 키


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;
    //사용자 소속 부서 키 (상위 엔티티 삭제시 NULL화)


    @Column(name = "member_account", nullable = false)
    private String memberAccount;
    //사번 (아이디)

    @Column(name = "member_password", nullable = false)
    private String memberPassword;
    //사용자 비밀번호

    @Column(name = "member_name", nullable = false)
    private String memberName;
    //사용자 이름

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false)
    private MemberRole memberRole;
    //사용자 권한

    @Enumerated(EnumType.STRING)
    @Column(name = "member_status", nullable = false)
    private MemberStatus memberStatus;
    //사용자 상태
}
