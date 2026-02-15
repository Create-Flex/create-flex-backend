package com.mcn.in4.domain.ai.tools;

import com.mcn.in4.domain.ai.dto.vacation.VacationQuery;
import com.mcn.in4.domain.ai.dto.vacation.VacationSummary;
import com.mcn.in4.domain.ai.service.AiChatServiceImpl;
import com.mcn.in4.domain.vacation.dto.response.AdminVacationListResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationListResponseDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationRemainderResponseDTO;
import com.mcn.in4.domain.vacation.service.VacationAdminService;
import com.mcn.in4.domain.vacation.service.VacationService;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 휴가 관련 AI 도구
 * - getMyVacationSummary: 본인의 잔여 연차 및 휴가 내역 조회 (EMPLOYEE, MANAGER,
 * ADMINISTRATOR)
 * - getAllVacationSummary: 전체/특정 직원 휴가 조회 (ADMINISTRATOR 전용)
 * - 크리에이터(CREATOR)는 ROLE_TOOLS 맵에서 제외되어 접근 불가
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class VacationTools {

    private final VacationService vacationService;
    private final VacationAdminService vacationAdminService;

    /** 자기 지칭 단어 목록 — targetName에 들어오면 null로 처리 */
    private static final Set<String> SELF_REFERENCES = Set.of(
            "나", "내", "본인", "저", "나의", "내꺼", "제", "자신");

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

    // ===== 날짜 범위 계산 공통 메서드 =====
    private LocalDate[] resolveDateRange(VacationQuery query) {
        LocalDate start = null;
        LocalDate end = null;

        if (query.dateInfo() != null && !query.dateInfo().isBlank()) {
            String info = query.dateInfo().toUpperCase().replace(" ", "_");
            LocalDate now = LocalDate.now();

            switch (info) {
                case "TODAY" -> {
                    start = now;
                    end = now;
                }
                case "THIS_WEEK" -> {
                    start = now.with(DayOfWeek.MONDAY);
                    end = now;
                }
                case "LAST_WEEK" -> {
                    start = now.minusWeeks(1).with(DayOfWeek.MONDAY);
                    end = now.minusWeeks(1).with(DayOfWeek.SUNDAY);
                }
                case "THIS_MONTH" -> {
                    start = now.withDayOfMonth(1);
                    end = now.withDayOfMonth(now.lengthOfMonth());
                }
                case "LAST_MONTH" -> {
                    start = now.minusMonths(1).withDayOfMonth(1);
                    end = now.minusMonths(1).withDayOfMonth(now.minusMonths(1).lengthOfMonth());
                }
                case "THIS_YEAR" -> {
                    start = now.withDayOfYear(1);
                    end = now.withDayOfYear(now.lengthOfYear());
                }
                default -> log.warn("Unknown dateInfo: {}", info);
            }
        }

        if (start == null)
            start = parseDate(query.startDate());
        if (end == null)
            end = parseDate(query.endDate());

        // 기본값: 올해 전체
        if (start == null)
            start = LocalDate.now().withDayOfMonth(1).withMonth(1);
        if (end == null)
            end = LocalDate.now().withDayOfMonth(31).withMonth(12);

        return new LocalDate[] { start, end };
    }

    // ===== 내 휴가 조회 도구 =====
    @Bean
    @Description("본인의 잔여 연차와 휴가 사용 내역을 조회합니다. 주어가 '나'일 때만 사용.")
    public Function<VacationQuery, VacationSummary> getMyVacationSummary() {
        return query -> {
            Authentication auth = getAuthOrThrow();

            // 자기 지칭("나", "본인" 등)은 targetName에서 무시
            String targetName = query.targetName();
            if (targetName != null && SELF_REFERENCES.contains(targetName.trim())) {
                log.info("자기 지칭 targetName='{}' 무시 처리", targetName);
                targetName = null;
            }

            // 타 직원 조회 시도 차단
            if (targetName != null && !targetName.isBlank()) {
                log.warn("getMyVacationSummary에 targetName='{}' 전달됨 - 권한 없음으로 차단", targetName);
                AttendanceTools.DENIED_MESSAGE.set(AiChatServiceImpl.MSG_NO_PERMISSION);
                return new VacationSummary(auth.getName(), AiChatServiceImpl.MSG_NO_PERMISSION, List.of());
            }

            Long currentMemberId;
            try {
                currentMemberId = Long.parseLong(auth.getName());
            } catch (NumberFormatException e) {
                log.error("Invalid user ID in security context", e);
                return new VacationSummary("Unknown", "사용자 정보를 확인할 수 없습니다.", List.of());
            }

            // 잔여 연차 조회
            VacationRemainderResponseDTO remainder = vacationService.getMyVacationRemainder(currentMemberId);

            // 휴가 내역 조회
            LocalDate[] range = resolveDateRange(query);
            List<VacationListResponseDTO> vacations = vacationService.getMyVacations(
                    currentMemberId, range[0], range[1], null);

            log.info("AI requesting MY vacation for memberId: {}, range: {} ~ {}, found: {}건",
                    currentMemberId, range[0], range[1], vacations.size());

            // 잔여 연차 정보 포맷
            String remainderInfo = String.format("총 연차: %.1f일, 사용: %.1f일, 잔여: %.1f일",
                    remainder.getTotalVacation(),
                    remainder.getUsedVacation(),
                    remainder.getVacationRemainder());

            // 휴가 내역 포맷
            List<String> records = vacations.stream()
                    .map(v -> String.format("[%s] %s, 사용일수: %.1f일, 사유: %s, 상태: %s",
                            v.getVacationPeriod(),
                            translateType(v.getVacationType()),
                            v.getVacationDays(),
                            v.getVacationDetail() != null ? v.getVacationDetail() : "-",
                            translateApprove(v.getVacationApprove())))
                    .collect(Collectors.toList());

            String message = records.isEmpty()
                    ? remainderInfo
                    : String.format("%s | %s ~ %s 기간 총 %d건의 휴가 기록이 있습니다.", remainderInfo, range[0], range[1],
                            records.size());

            return new VacationSummary(String.valueOf(currentMemberId), message, records);
        };
    }

    // ===== 전체 직원 휴가 조회 도구 (관리자 전용) =====
    @Bean
    @Description("전체 또는 특정 직원의 휴가를 조회합니다. 관리자 전용. 특정 이름 언급 시 targetName에 이름을 넣어 호출.")
    public Function<VacationQuery, VacationSummary> getAllVacationSummary() {
        return query -> {
            Authentication auth = getAuthOrThrow();

            // 관리자 권한 확인
            if (!hasRole(auth, "ADMINISTRATOR")) {
                log.warn("Non-admin user {} attempted to access all vacations", auth.getName());
                return new VacationSummary(auth.getName(),
                        AiChatServiceImpl.MSG_NO_PERMISSION, List.of());
            }

            LocalDate[] range = resolveDateRange(query);

            log.info("AI requesting ALL vacation by admin: {}, targetName: {}, range: {} ~ {}",
                    auth.getName(), query.targetName(), range[0], range[1]);

            // 관리자용 전체 휴가 조회 (이름 검색 지원)
            Page<AdminVacationListResponseDTO> resultPage = vacationAdminService.getVacationList(
                    range[0], range[1], null, query.targetName(), null,
                    PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "vacationStart")));

            List<String> records = resultPage.getContent().stream()
                    .map(v -> {
                        String period = v.getVacationStart().equals(v.getVacationEnd())
                                ? v.getVacationStart().toString()
                                : v.getVacationStart() + " ~ " + v.getVacationEnd();
                        return String.format("[%s] %s - 유형: %s, 사용일수: %.1f일, 잔여: %.1f일, 상태: %s",
                                period,
                                v.getMemberName() != null ? v.getMemberName() : "이름없음",
                                translateType(v.getVacationType()),
                                v.getVacationDays(),
                                v.getVacationRemainder() != null ? v.getVacationRemainder() : 0.0,
                                translateApprove(v.getVacationApprove()));
                    })
                    .collect(Collectors.toList());

            String message;
            if (records.isEmpty()) {
                message = (query.targetName() != null)
                        ? String.format("'%s'님의 %s ~ %s 기간 휴가 기록이 없습니다.", query.targetName(), range[0], range[1])
                        : String.format("%s ~ %s 기간에 조회된 전체 직원 휴가 기록이 없습니다.", range[0], range[1]);
            } else {
                message = (query.targetName() != null)
                        ? String.format("'%s'님의 %s ~ %s 기간 휴가 기록 %d건입니다.", query.targetName(), range[0], range[1],
                                records.size())
                        : String.format("%s ~ %s 기간 전체 직원 총 %d건의 휴가 기록이 있습니다.", range[0], range[1], records.size());
            }

            return new VacationSummary(auth.getName(), message, records);
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
            return LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);
        } catch (Exception e) {
            log.warn("Failed to parse date: {}", dateStr);
            return LocalDate.now();
        }
    }

    private String translateType(String typeStr) {
        try {
            return com.mcn.in4.domain.vacation.entity.enums.VacationType.valueOf(typeStr).getDescription();
        } catch (IllegalArgumentException e) {
            return typeStr;
        }
    }

    private String translateApprove(String approveStr) {
        try {
            return com.mcn.in4.domain.vacation.entity.enums.VacationApprove.valueOf(approveStr).getDescription();
        } catch (IllegalArgumentException e) {
            return approveStr;
        }
    }
}
