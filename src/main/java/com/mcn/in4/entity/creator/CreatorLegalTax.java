package com.mcn.in4.entity.creator;

import com.mcn.in4.entity.creator.creatorEnum.LegalTaxStatus;
import com.mcn.in4.entity.creator.creatorEnum.LegalTaxType;
import com.mcn.in4.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "creator_legal_tax") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreatorLegalTax {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "legal_tax_id")
    private Long legalTaxId;
    //크리에이터 계약 키

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_creator_id", nullable = false)
    private Member memberCreator;
    //크리에이터의 사용자 키

    @Enumerated(EnumType.STRING)
    @Column(name = "legal_tax_type", nullable = false)
    private LegalTaxType legalTaxType;
    //법률 / 세무 분류

    @Column(name = "legal_tax_name", nullable = false)
    private String legalTaxName;
    //법률세무 제목

    @Column(name = "legal_tax_detail", nullable = false)
    private String legalTaxDetail;
    //법률세무 상세

    @Enumerated(EnumType.STRING)
    @Column(name = "letal_tax_status", nullable = false)
    private LegalTaxStatus legalTaxStatus;
    //법률세무 상태
}
