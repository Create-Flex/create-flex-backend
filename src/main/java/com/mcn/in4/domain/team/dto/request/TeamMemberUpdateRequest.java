package com.mcn.in4.domain.team.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class TeamMemberUpdateRequest {
    private List<Long> memberIds;
}