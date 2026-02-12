package com.mcn.in4.domain.ai.dto.attendance;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonClassDescription("근태 조회 요청. 구체적 날짜는 startDate/endDate(YYYY-MM-DD)를 사용하고, 상대적 표현은 dateInfo를 사용.")
public record AttendanceQuery(
                @JsonPropertyDescription("조회 시작 날짜 (YYYY-MM-DD). 연도 미명시 시 2026년 기준.") String startDate,
                @JsonPropertyDescription("조회 종료 날짜 (YYYY-MM-DD). 연도 미명시 시 2026년 기준.") String endDate,
                @JsonPropertyDescription("상대적 기간: TODAY, YESTERDAY, THIS_WEEK, LAST_WEEK, THIS_MONTH, LAST_MONTH. 구체적 날짜가 있으면 startDate/endDate를 대신 사용.") String dateInfo,
                @JsonPropertyDescription("검색할 타인의 이름. '나', '본인', '내' 등 자기 지칭은 넣지 말 것. 타인 이름 언급 시에만 설정하고 getAllAttendanceSummary를 호출.") String targetName) {
}
