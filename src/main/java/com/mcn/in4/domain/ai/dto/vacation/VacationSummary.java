package com.mcn.in4.domain.ai.dto.vacation;

import java.util.List;

public record VacationSummary(
        String memberId,
        String message,
        List<String> records) {
}
