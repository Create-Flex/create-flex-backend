package com.mcn.in4.domain.QnA.service;

import org.springframework.stereotype.Service;
import com.mcn.in4.domain.QnA.dto.QAResponseDto.*;

import java.util.List;

public interface QAService {
    List<QATitle> generateQATitleList(Long memberId);
    QADetail generateQADetail(Long qaId);
}
