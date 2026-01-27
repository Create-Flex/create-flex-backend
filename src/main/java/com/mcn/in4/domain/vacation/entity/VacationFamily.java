package com.mcn.in4.domain.vacation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vacation_family")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VacationFamily {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_id")
    private Long familyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vacation_id", nullable = false)
    private Vacation vacation;

    @Column(name = "family_relation", nullable = false)
    private String familyRelation;

    @Column(name = "family_detail", nullable = false)
    private String familyDetail;
}
