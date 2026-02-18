package com.mcn.in4.domain.member.dto;

import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.entity.memberEnum.MemberStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberSummaryDto {
    private Long memberId;
    private String departmentName;
    private String memberName;
    private String task;
    private MemberRole memberRole;
    private MemberStatus memberStatus;
    private String profileImage;

    public static MemberSummaryDto from(Member member, String profileImage) {
        return MemberSummaryDto.builder()
                .memberId(member.getMemberId())
                .departmentName(member.getDepartment() != null ? member.getDepartment().getDepartmentName() : "크리에이터")
                .memberName(member.getMemberName())
                .task(member.getTask())
                .memberRole(member.getMemberRole())
                .memberStatus(member.getMemberStatus())
                .profileImage(profileImage)
                .build();
    }

}
