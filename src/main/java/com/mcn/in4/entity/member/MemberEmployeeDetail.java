package com.mcn.in4.entity.member;

import com.mcn.in4.entity.member.memberEnum.EmploymentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee_detail") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberEmployeeDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_detail_id")
    private Long employeeDetailId;
    //직원상세 키

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Member member;
    //직원의 사용자 키

    @Column(name = "task", nullable = true)
    private String task;
    //직무

    @Column(name = "nickname", nullable = true)
    private String nickname;
    //직원 닉네임

    @Column(name = "eng_name", nullable = true)
    private String engName;
    //직원 영문명

    @Column(name = "personal_email", nullable = false)
    private String personalEmail;
    //직원 개인 이메일

    @Column(name = "personal_call", nullable = false)
    private String personalCall;
    //직원 개인 휴대전화

    @Column(name = "hire_date", nullable = false)
    private String hireDate;
    //직원 입사일

    @Column(name = "emp_date", nullable = true)
    private String empDate;
    //직원 퇴사일

    @Column(name = "address", nullable = true)
    private String address;
    //직원 주소

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = true)
    private EmploymentType employmentType;
    //직원 입사 타입

    @Column(name = "leaving_reason", nullable = true)
    private String leavingReason;
    //직원 퇴사 사유
}
