package com.mcn.in4.domain.ai.dto.attendance;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonClassDescription("근태 조회 요청. 'N월 N일' 같은 구체적 날짜는 startDate/endDate를 YYYY-MM-DD로 변환하여 사용. '이번주','저번달' 같은 상대 표현만 dateInfo 사용.")
public record AttendanceQuery(
                @JsonPropertyDescription("조회 시작 날짜 (YYYY-MM-DD 형식). 1월=01, 2월=02, ..., 12월=12. 예: '1월 1일'→'2026-01-01', '3월 15일'→'2026-03-15'. 사용자가 말한 월(月) 숫자를 그대로 사용할 것.") String startDate,
                @JsonPropertyDescription("조회 종료 날짜 (YYYY-MM-DD 형식). 1월=01, 2월=02, ..., 12월=12. 예: '1월 10일'→'2026-01-10', '2월 28일'→'2026-02-28'. 사용자가 말한 월(月) 숫자를 그대로 사용할 것.") String endDate,
                @JsonPropertyDescription("상대적 기간. 가능한 값: 'TODAY'(오늘), 'YESTERDAY'(어제), 'THIS_WEEK'(이번주), 'LAST_WEEK'(저번주), 'THIS_MONTH'(이번달), 'LAST_MONTH'(저번달). 구체적 날짜(N월 N일)가 있으면 이 필드 대신 startDate/endDate를 사용하세요.") String dateInfo,
                @JsonPropertyDescription("검색할 직원 이름. '홍길동 근태 보여줘'처럼 특정 이름이 언급되면 반드시 이 필드에 이름을 넣고 'getAllAttendanceSummary'를 호출하세요. (주의: 이름이 있으면 절대 'getMyAttendanceSummary'를 사용하지 마세요)") String targetName) {
}
