package com.mcn.in4.domain.schedule.dto.responseDTO;

import com.mcn.in4.domain.schedule.entity.scheduleEnum.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class SchedulReponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScheduleResponseDto {
        private String scheduleName;
        private LocalDate scheduleDate;
        private String scheduleDetail;
        private ScheduleType scheduleType;
    }
}
