package com.mcn.in4.domain.department.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "department") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;
    //부서 키

    @Column(name = "department_name", nullable = false)
    private String departmentName;
    //부서명

    @Column(name = "department_call", nullable = false)
    private String departmentCall;
    //부서 전화번호

    @Column(name = "department_detail")
    private String departmentDetail;

    @Column(name = "department_color", nullable = false)
    private String departmentColor;
}
