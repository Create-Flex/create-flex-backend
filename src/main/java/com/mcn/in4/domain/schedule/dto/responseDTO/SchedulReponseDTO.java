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
        private Long scheduleId;
        private String scheduleName; // 제목
        private LocalDate scheduleDate; // 날짜
        private String scheduleDetail; // 상세
        private ScheduleType scheduleType; // 개인, 회사 등등
    }
}
