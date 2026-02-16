package com.mcn.in4.domain.ai.dto.schedule;

import java.util.List;

public record ScheduleSummary(
        String targetName,
        String message,
        List<String> schedules) {
}
