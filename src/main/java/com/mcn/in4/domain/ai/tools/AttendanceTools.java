package com.mcn.in4.domain.ai.tools;

import com.mcn.in4.domain.ai.service.AiChatServiceImpl;

import com.mcn.in4.domain.ai.dto.attendance.AttendanceQuery;
import com.mcn.in4.domain.ai.dto.attendance.AttendanceSummary;
import com.mcn.in4.domain.attendance.dto.AttendanceResponseDto;
import com.mcn.in4.domain.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AttendanceTools {

    private final AttendanceService attendanceService;

    /** 자기 지칭 단어 목록 — targetName에 들어오면 null로 처리 */
    private static final Set<String> SELF_REFERENCES = Set.of(
            "나", "내", "본인", "저", "나의", "내꺼", "제", "자신");

    /**
     * 도구에서 권한 거부 시, LLM 응답을 무시하고 이 메시지를 직접 반환하기 위한 플래그.
     * AiChatServiceImpl에서 LLM 호출 후 이 값을 확인합니다.
     */
    public static final ThreadLocal<String> DENIED_MESSAGE = new ThreadLocal<>();

    // ===== 날짜 범위 계산 공통 메서드 =====
    private LocalDate[] resolveDateRange(AttendanceQuery query) {
        LocalDate start = null;
        LocalDate end = null;

        // dateInfo가 있으면 우선 사용
        if (query.dateInfo() != null && !query.dateInfo().isBlank()) {
            String info = query.dateInfo().toUpperCase().replace(" ", "_");
            LocalDate now = LocalDate.now();

            switch (info) {
                case "TODAY" -> {
                    start = now;
                    end = now;
                }
                case "YESTERDAY" -> {
                    start = now.minusDays(1);
                    end = now.minusDays(1);
                }
                case "THIS_WEEK" -> {
                    start = now.with(java.time.DayOfWeek.MONDAY);
                    end = now;
                }
                case "LAST_WEEK" -> {
                    start = now.minusWeeks(1).with(java.time.DayOfWeek.MONDAY);
                    end = now.minusWeeks(1).with(java.time.DayOfWeek.SUNDAY);
                }
                case "THIS_MONTH" -> {
                    start = now.withDayOfMonth(1);
                    end = now;
                }
                case "LAST_MONTH" -> {
                    start = now.minusMonths(1).withDayOfMonth(1);
                    end = now.minusMonths(1).withDayOfMonth(now.minusMonths(1).lengthOfMonth());
                }
                default -> log.warn("Unknown dateInfo: {}", info);
            }
        }

        // dateInfo로 안 잡혔으면 직접 지정 날짜 사용
        if (start == null)
            start = parseDate(query.startDate());
        if (end == null)
            end = parseDate(query.endDate());

        // 여전히 없으면 오늘 날짜
        if (start == null)
            start = LocalDate.now();
        if (end == null)
            end = LocalDate.now();

        return new LocalDate[] { start, end };
    }

    // ===== 인증 정보 추출 공통 메서드 =====
    private Authentication getAuthOrThrow() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal().equals("anonymousUser")) {
            throw new IllegalStateException("로그인이 필요한 서비스입니다.");
        }
        return auth;
    }

    private boolean hasRole(Authentication auth, String role) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_" + role));
    }

    // ===== 내 근태 조회 도구 =====
    @Bean
    @Description("본인의 근태 기록(출퇴근 시간, 상태)을 조회합니다. 주어가 '나'일 때만 사용.")
    public Function<AttendanceQuery, AttendanceSummary> getMyAttendanceSummary() {
        return query -> {
            Authentication auth = getAuthOrThrow();

            // 자기 지칭("나", "본인" 등)은 targetName에서 무시
            String targetName = query.targetName();
            if (targetName != null && SELF_REFERENCES.contains(targetName.trim())) {
                log.info("자기 지칭 targetName='{}' 무시 처리", targetName);
                targetName = null;
            }

            // 타 직원 조회 시도 차단: targetName이 있으면 즉시 거부 (DB 쿼리 안 함)
            if (targetName != null && !targetName.isBlank()) {
                log.warn("getMyAttendanceSummary에 targetName='{}' 전달됨 - 권한 없음으로 차단", targetName);
                DENIED_MESSAGE.set(AiChatServiceImpl.MSG_NO_PERMISSION);
                return new AttendanceSummary(auth.getName(), AiChatServiceImpl.MSG_NO_PERMISSION, List.of());
            }

            Long currentMemberId;
            try {
                currentMemberId = Long.parseLong(auth.getName());
            } catch (NumberFormatException e) {
                log.error("Invalid user ID in security context", e);
                return new AttendanceSummary("Unknown", "사용자 정보를 확인할 수 없습니다.", List.of());
            }

            LocalDate[] range = resolveDateRange(query);
            LocalDate start = range[0], end = range[1];

            log.info("AI requesting MY attendance for memberId: {}, dateInfo: {}, range: {} ~ {}",
                    currentMemberId, query.dateInfo(), start, end);

            Page<AttendanceResponseDto> resultPage = attendanceService.getAttendance(
                    currentMemberId, start, end, null,
                    PageRequest.of(0, 30, Sort.by(Sort.Direction.DESC, "attendanceDate")));

            List<String> summaryRecords = resultPage.getContent().stream()
                    .map(dto -> String.format("[%s] 출근: %s, 퇴근: %s, 상태: %s/%s",
                            dto.getAttendanceDate(),
                            dto.getAttendanceStart() != null
                                    ? dto.getAttendanceStart().toLocalTime().toString().substring(0, 5)
                                    : "미출근",
                            dto.getAttendanceEnd() != null
                                    ? dto.getAttendanceEnd().toLocalTime().toString().substring(0, 5)
                                    : "미퇴근",
                            dto.getCheckInStatus() != null ? dto.getCheckInStatus() : "-",
                            dto.getCheckOutStatus() != null ? dto.getCheckOutStatus() : "-"))
                    .collect(Collectors.toList());

            String message = summaryRecords.isEmpty()
                    ? String.format("%s ~ %s 기간에 조회된 근태 기록이 없습니다.", start, end)
                    : String.format("%s부터 %s까지 총 %d건의 기록이 있습니다.", start, end, summaryRecords.size());

            return new AttendanceSummary(String.valueOf(currentMemberId), message, summaryRecords);
        };
    }

    // ===== 전체 직원 근태 조회 도구 (관리자 전용) =====
    @Bean
    @Description("전체 또는 특정 직원의 근태를 조회합니다. 관리자 전용. 특정 이름 언급 시 targetName에 이름을 넣어 호출.")
    public Function<AttendanceQuery, AttendanceSummary> getAllAttendanceSummary() {
        return query -> {
            Authentication auth = getAuthOrThrow();

            // 관리자 권한 확인
            if (!hasRole(auth, "ADMINISTRATOR")) {
                log.warn("Non-admin user {} attempted to access all attendance", auth.getName());
                return new AttendanceSummary(auth.getName(),
                        AiChatServiceImpl.MSG_NO_PERMISSION, List.of());
            }

            LocalDate[] range = resolveDateRange(query);
            LocalDate start = range[0], end = range[1];

            log.info("AI requesting ALL attendance by admin: {}, targetName: {}, dateInfo: {}, range: {} ~ {}",
                    auth.getName(), query.targetName(), query.dateInfo(), start, end);

            // 전체 직원 근태 조회 (이름 검색 지원)
            Page<AttendanceResponseDto> resultPage = attendanceService.getAllAttendance(
                    start, end, null, query.targetName(), // 이름 검색어 전달
                    PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "attendanceDate")));

            // 직원 이름 포함하여 요약
            List<String> summaryRecords = resultPage.getContent().stream()
                    .map(dto -> String.format("[%s] %s - 출근: %s, 퇴근: %s, 상태: %s/%s",
                            dto.getAttendanceDate(),
                            dto.getMemberName() != null ? dto.getMemberName() : "이름없음",
                            dto.getAttendanceStart() != null
                                    ? dto.getAttendanceStart().toLocalTime().toString().substring(0, 5)
                                    : "미출근",
                            dto.getAttendanceEnd() != null
                                    ? dto.getAttendanceEnd().toLocalTime().toString().substring(0, 5)
                                    : "미퇴근",
                            dto.getCheckInStatus() != null ? dto.getCheckInStatus() : "-",
                            dto.getCheckOutStatus() != null ? dto.getCheckOutStatus() : "-"))
                    .collect(Collectors.toList());

            String message;
            if (summaryRecords.isEmpty()) {
                message = (query.targetName() != null)
                        ? String.format("'%s'님의 %s ~ %s 기간 근태 기록이 없습니다.", query.targetName(), start, end)
                        : String.format("%s ~ %s 기간에 조회된 전체 직원 근태 기록이 없습니다.", start, end);
            } else {
                message = (query.targetName() != null)
                        ? String.format("'%s'님의 %s ~ %s 기간 근태 기록 %d건입니다.", query.targetName(), start, end,
                                summaryRecords.size())
                        : String.format("%s부터 %s까지 전체 직원 총 %d건의 근태 기록이 있습니다.", start, end, summaryRecords.size());
            }

            return new AttendanceSummary(auth.getName(), message, summaryRecords);
        };
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank() || "null".equalsIgnoreCase(dateStr)) {
            return null;
        }
        try {
            if (dateStr.contains("-")) {
                return LocalDate.parse(dateStr);
            }
            return LocalDate.parse(dateStr, java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
        } catch (Exception e) {
            log.warn("Failed to parse date: {}", dateStr);
            return LocalDate.now();
        }
    }
}
