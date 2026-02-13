package com.mcn.in4.domain.QnA.service;

import com.mcn.in4.domain.QnA.dto.QAResponseDto.*;

import java.util.List;

public interface QAService {
    List<QATitle> generateQATitleList(Long memberId);

    QADetail generateQADetail(Long qaId);

    void uploadQuestion(Long memberId, String questionTitle, String questionDetail);

    void uploadAnswer(Long memberId, Long qaId, String answerDetail);
}