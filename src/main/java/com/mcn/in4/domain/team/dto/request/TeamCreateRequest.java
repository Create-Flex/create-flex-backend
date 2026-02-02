package com.mcn.in4.domain.team.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class TeamCreateRequest {
    private String teamName;
    private String teamDetail;
    private List<Long> memberIds;
}