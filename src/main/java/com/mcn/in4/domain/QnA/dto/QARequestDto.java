package com.mcn.in4.domain.QnA.dto;

import lombok.*;

public class QARequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class QuestionDto {
        private String questionTitle;
        private String questionDetail;
    }
}
