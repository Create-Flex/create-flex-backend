package com.mcn.in4.domain.QnA.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class QARequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class QuestionDto {
        private String questionTitle;
        private String questionDetail;
        private List<MultipartFile> files;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class AnswerDto {
        private Long qaId;
        private String answerTitle;
        private String answerDetail;
    }
}
