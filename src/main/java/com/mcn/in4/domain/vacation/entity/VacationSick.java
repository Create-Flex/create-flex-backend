package com.mcn.in4.domain.vacation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vacation_sick")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VacationSick {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sick_id")
    private Long sickId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vacation_id", nullable = false)
    private Vacation vacation;

    @Column(name = "sick_detail", nullable = false)
    private String sickDetail;

    @Column(name = "sick_hospital", nullable = false)
    private String sickHospital;
}
