package com.mcn.in4.domain.ai.dto.attendance;

import java.util.List;

public record AttendanceSummary(
        String memberId,
        String message,
        List<String> records) {
}
