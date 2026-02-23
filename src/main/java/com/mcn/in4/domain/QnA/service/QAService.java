package com.mcn.in4.domain.QnA.service;

import com.mcn.in4.domain.QnA.dto.QAResponseDto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QAService {
    List<QATitle> generateQATitleList(Long memberId, Long listPage);

    QADetail generateQADetail(Long qaId);

    QAFileUpload uploadQuestion(Long memberId, String questionTitle, String questionDetail, List<MultipartFile> files);

    void uploadAnswer(Long memberId, Long qaId, String answerDetail);
}