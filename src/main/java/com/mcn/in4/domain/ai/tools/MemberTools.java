package com.mcn.in4.domain.ai.tools;

import com.mcn.in4.domain.ai.dto.member.MemberQuery;
import com.mcn.in4.domain.ai.dto.member.MemberSummaryResult;
import com.mcn.in4.domain.member.dto.MemberSummaryDto;
import com.mcn.in4.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 직원 검색 AI 도구
 * - searchMember: 이름이나 부서명으로 직원 검색 (EMPLOYEE, MANAGER, ADMINISTRATOR)
 * - 크리에이터(CREATOR)는 ROLE_TOOLS 맵에서 제외되어 접근 불가
 * - 반환 정보: 이름, 부서, 직무, 역할 (민감정보 미포함)
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class MemberTools {

    private final MemberService memberService;

    // ===== 인증 정보 추출 =====
    private Authentication getAuthOrThrow() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal().equals("anonymousUser")) {
            throw new IllegalStateException("로그인이 필요한 서비스입니다.");
        }
        return auth;
    }

    // ===== 직원 검색 도구 =====
    @Bean
    @Description("직원을 이름이나 부서명으로 검색합니다. 예: '마케팅팀 팀장이 누구야?', '김철수 어느 부서야?'")
    public Function<MemberQuery, MemberSummaryResult> searchMember() {
        return query -> {
            Authentication auth = getAuthOrThrow();
            log.info("AI 직원 검색 요청 - 이름: {}, 부서: {}, 요청자: {}",
                    query.name(), query.departmentName(), auth.getName());

            List<MemberSummaryDto> allMembers = memberService.getAllMembers();

            // 이름 또는 부서 필터링
            List<MemberSummaryDto> filtered = allMembers.stream()
                    .filter(m -> {
                        boolean nameMatch = true;
                        boolean deptMatch = true;

                        if (query.name() != null && !query.name().isBlank()) {
                            nameMatch = m.getMemberName() != null
                                    && m.getMemberName().contains(query.name().trim());
                        }
                        if (query.departmentName() != null && !query.departmentName().isBlank()) {
                            deptMatch = m.getDepartmentName() != null
                                    && m.getDepartmentName().contains(query.departmentName().trim());
                        }
                        return nameMatch && deptMatch;
                    })
                    .collect(Collectors.toList());

            log.info("AI 직원 검색 결과: {}건", filtered.size());

            List<String> records = filtered.stream()
                    .map(m -> String.format("이름: %s, 부서: %s, 직무: %s, 역할: %s, 상태: %s",
                            m.getMemberName(),
                            m.getDepartmentName() != null ? m.getDepartmentName() : "-",
                            m.getTask() != null ? m.getTask() : "-",
                            m.getMemberRole() != null ? m.getMemberRole().name() : "-",
                            m.getMemberStatus() != null ? m.getMemberStatus().name() : "-"))
                    .collect(Collectors.toList());

            String message;
            if (records.isEmpty()) {
                String searchTerm = "";
                if (query.name() != null && !query.name().isBlank())
                    searchTerm += "이름='" + query.name() + "' ";
                if (query.departmentName() != null && !query.departmentName().isBlank())
                    searchTerm += "부서='" + query.departmentName() + "'";
                message = String.format("검색 조건(%s)에 해당하는 직원을 찾을 수 없습니다.", searchTerm.trim());
            } else {
                message = String.format("총 %d명의 직원이 검색되었습니다.", records.size());
            }

            return new MemberSummaryResult(message, records);
        };
    }
}
