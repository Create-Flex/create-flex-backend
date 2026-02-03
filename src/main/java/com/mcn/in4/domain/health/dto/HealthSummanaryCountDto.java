package com.mcn.in4.domain.health.dto;

import com.mcn.in4.domain.health.entity.CheckupSummanary;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class HealthSummanaryCountDto {
    private CheckupSummanary checkupSummanary;
    private Long totalCount;

    public HealthSummanaryCountDto(CheckupSummanary checkupSummanary, Long totalCount) {
        this.checkupSummanary = checkupSummanary;
        this.totalCount = totalCount;
    }
}
