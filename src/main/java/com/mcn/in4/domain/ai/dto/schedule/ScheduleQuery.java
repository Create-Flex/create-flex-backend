package com.mcn.in4.domain.ai.dto.schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ScheduleQuery(
        @JsonPropertyDescription("조회 시작 날짜 (YYYY-MM-DD), dateInfo가 있으면 생략 가능") String startDate,

        @JsonPropertyDescription("조회 종료 날짜 (YYYY-MM-DD), dateInfo가 있으면 생략 가능") String endDate,

        @JsonPropertyDescription("상대적 날짜 표현 (TODAY, THIS_WEEK, NEXT_WEEK, THIS_MONTH, LAST_MONTH, THIS_YEAR)") String dateInfo,

        @JsonPropertyDescription("조회 대상 이름 (크리에이터 일정 조회 시 사용, 내 일정 조회 시 생략)") String targetName) {
}
