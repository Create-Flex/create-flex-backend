package com.mcn.in4.domain.health.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Builder
public class MentalHealthDto {
    private Long memberId;
    private LocalDate creatorMentalDate;
    private Long creatorMentalScore;

    public MentalHealthDto(Long memberId, LocalDate creatorMentalDate, Long creatorMentalScore) {
        this.memberId = memberId;
        this.creatorMentalDate = creatorMentalDate;
        this.creatorMentalScore = creatorMentalScore;
    }
}
