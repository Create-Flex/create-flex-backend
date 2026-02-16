package com.mcn.in4.domain.ai.tools;

import com.mcn.in4.domain.ai.dto.schedule.ScheduleQuery;
import com.mcn.in4.domain.ai.dto.schedule.ScheduleSummary;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.schedule.dto.responseDTO.SchedulReponseDTO;
import com.mcn.in4.domain.schedule.service.SchedulService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ScheduleTools {

    private final SchedulService schedulService;

    @Bean
    @Description("일반 직원, 매니저, 관리자의 '나의 일정'(개인/회사)을 조회합니다. (크리에이터 사용 불가)")
    public Function<ScheduleQuery, ScheduleSummary> getMySchedule() {
        return query -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String role = auth.getAuthorities().iterator().next().getAuthority();
            Long memberId = Long.parseLong(auth.getName());

            // 크리에이터는 '나의 일정(개인/회사)' 조회 불가
            if ("ROLE_CREATOR".equals(role)) {
                return new ScheduleSummary("나", "크리에이터는 '나의 일정'을 조회할 수 없습니다. '크리에이터 일정'을 조회해주세요.", List.of());
            }

            LocalDate[] range = resolveDateRange(query);
            LocalDate start = range[0];
            LocalDate end = range[1];

            List<String> months = getMonthsBetween(start, end);
            List<SchedulReponseDTO.ScheduleResponseDto> allSchedules = new ArrayList<>();

            for (String month : months) {
                try {
                    allSchedules.addAll(schedulService.getMyMonthlySchedules(memberId, month));
                } catch (Exception e) {
                    log.warn("Failed to fetch schedules for month {}: {}", month, e.getMessage());
                }
            }

            List<String> validSchedules = allSchedules.stream()
                    .filter(s -> !s.getScheduleDate().isBefore(start) && !s.getScheduleDate().isAfter(end))
                    .sorted((s1, s2) -> s1.getScheduleDate().compareTo(s2.getScheduleDate()))
                    .map(s -> String.format("[%s] (%s) %s %s",
                            s.getScheduleDate(),
                            s.getScheduleType().getDescription(),
                            s.getScheduleName(),
                            s.getScheduleDetail() != null ? "- " + s.getScheduleDetail() : ""))
                    .toList();

            String message = validSchedules.isEmpty()
                    ? String.format("%s ~ %s 기간 동안 등록된 일정이 없습니다.", start, end)
                    : String.format("%s ~ %s 기간 동안 총 %d건의 일정이 있습니다.", start, end, validSchedules.size());

            return new ScheduleSummary("나", message, validSchedules);
        };
    }

    @Bean
    @Description("크리에이터(본인) 및 매니저(담당)의 '크리에이터 일정'(방송/콘텐츠 등)을 조회합니다.")
    public Function<ScheduleQuery, ScheduleSummary> getCreatorSchedule() {
        return query -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String role = auth.getAuthorities().iterator().next().getAuthority();
            Long memberId = Long.parseLong(auth.getName());

            // 관리자 및 접근 권한 없는 역할 체크 (이미 ROLE_TOOLS에서 걸러지긴 하지만 이중 체크)
            if (!"ROLE_CREATOR".equals(role) && !"ROLE_MANAGER".equals(role)) {
                return new ScheduleSummary("크리에이터", "권한이 없습니다. 매니저 또는 크리에이터만 조회 가능합니다.", List.of());
            }

            LocalDate[] range = resolveDateRange(query);
            LocalDate start = range[0];
            LocalDate end = range[1];

            List<String> months = getMonthsBetween(start, end);
            List<SchedulReponseDTO.ScheduleResponseDto> allSchedules = new ArrayList<>();

            for (String month : months) {
                try {
                    allSchedules.addAll(schedulService.getCreatorSchedules(memberId, role, month));
                } catch (Exception e) {
                    log.warn("Failed to fetch creator schedules for month {}: {}", month, e.getMessage());
                }
            }

            Stream<SchedulReponseDTO.ScheduleResponseDto> stream = allSchedules.stream()
                    .filter(s -> !s.getScheduleDate().isBefore(start) && !s.getScheduleDate().isAfter(end));

            // 매니저가 특정 크리에이터 이름을 지정한 경우 필터링
            if ("ROLE_MANAGER".equals(role) && query.targetName() != null && !query.targetName().isBlank()) {
                String target = query.targetName().trim();
                stream = stream.filter(s -> s.getCreatorName() != null && s.getCreatorName().contains(target));
            }

            List<String> validSchedules = stream
                    .sorted((s1, s2) -> s1.getScheduleDate().compareTo(s2.getScheduleDate()))
                    .map(s -> String.format("[%s] [%s] (%s) %s %s",
                            s.getScheduleDate(),
                            s.getCreatorName() != null ? s.getCreatorName() : "미지정",
                            s.getScheduleType().getDescription(),
                            s.getScheduleName(),
                            s.getScheduleDetail() != null ? "- " + s.getScheduleDetail() : ""))
                    .toList();

            String targetDisplay = query.targetName() != null ? query.targetName() : "담당 크리에이터";
            String message = validSchedules.isEmpty()
                    ? String.format("%s ~ %s 기간 동안 %s의 일정이 없습니다.", start, end, targetDisplay)
                    : String.format("%s ~ %s 기간 동안 %s의 총 %d건의 일정이 있습니다.", start, end, targetDisplay,
                            validSchedules.size());

            return new ScheduleSummary(targetDisplay, message, validSchedules);
        };
    }

    private LocalDate[] resolveDateRange(ScheduleQuery query) {
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
                    start = now.with(java.time.DayOfWeek.MONDAY);
                    end = now.with(java.time.DayOfWeek.SUNDAY);
                }
                case "NEXT_WEEK" -> {
                    start = now.plusWeeks(1).with(java.time.DayOfWeek.MONDAY);
                    end = now.plusWeeks(1).with(java.time.DayOfWeek.SUNDAY);
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
            }
        }

        if (start == null)
            start = parseDate(query.startDate());
        if (end == null)
            end = parseDate(query.endDate());

        // 기본값: 오늘 ~ 이번달 말
        if (start == null)
            start = LocalDate.now();
        if (end == null)
            end = start.withDayOfMonth(start.lengthOfMonth());

        return new LocalDate[] { start, end };
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

    private List<String> getMonthsBetween(LocalDate start, LocalDate end) {
        List<String> months = new ArrayList<>();
        LocalDate current = start.withDayOfMonth(1);
        LocalDate limit = end.withDayOfMonth(1);

        while (!current.isAfter(limit)) {
            months.add(current.format(DateTimeFormatter.ofPattern("yyyy-MM")));
            current = current.plusMonths(1);
        }
        return months;
    }
}
