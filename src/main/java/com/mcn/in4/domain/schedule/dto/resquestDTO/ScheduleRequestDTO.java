package com.mcn.in4.domain.schedule.dto.resquestDTO;

import com.mcn.in4.domain.schedule.entity.scheduleEnum.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class ScheduleRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleCreateRequestDto {
        private String scheduleName; //제목
        private LocalDate scheduleDate; // 날짜
        private String scheduleDetail; // 상세
        private ScheduleType scheduleType; // 이넘 타입
    }
}
