package com.mcn.in4.domain.ai.dto.member;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonClassDescription("직원 검색 요청. 이름, 부서명, 직무 등으로 검색 가능.")
public record MemberQuery(
        @JsonPropertyDescription("검색할 직원의 이름 (부분 일치 가능). 예: '김철수', '김'") String name,
        @JsonPropertyDescription("검색할 부서명 (부분 일치 가능). 예: '마케팅', '개발'") String departmentName) {
}
