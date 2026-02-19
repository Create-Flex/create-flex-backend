package com.mcn.in4.domain.QnA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class QAResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QATitle{
        private Long qaId;
        private String departmentName;
        private String questionTitle;
        private LocalDateTime questionTime;
        private String questionMemberName;
        private boolean answered;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QADetail{
        private Long qaId;
        private String questionTitle;
        private LocalDateTime questionTime;
        private String questionMemberName;
        private String questionDepartmentName;
        private String questionDetail;
        private boolean answered;

        private LocalDateTime answerTime;
        private String answerMemberName;
        private String answerDepartmentName;
        private String answerDetail;
    }
}
