package com.mcn.in4.domain.ai.dto.member;

import java.util.List;

public record MemberSummaryResult(
        String message,
        List<String> records) {
}
