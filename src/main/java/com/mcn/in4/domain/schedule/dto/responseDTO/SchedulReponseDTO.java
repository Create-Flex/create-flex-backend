package com.mcn.in4.domain.schedule.dto.responseDTO;

import com.mcn.in4.domain.schedule.entity.scheduleEnum.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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

        private Long creatorId;// 크리에이터 아이디
        private String creatorName;//크리에이터 이름
        private List<Long> visitorIds; //합방 크리에이터 아이디들
        private List<String> visitorNames; // 합방 크리에이터 이름 들
    }
}
