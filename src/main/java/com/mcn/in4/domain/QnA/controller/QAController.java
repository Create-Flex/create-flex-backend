package com.mcn.in4.domain.QnA.controller;

import com.mcn.in4.domain.QnA.service.QAService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.mcn.in4.domain.QnA.service.QAService;
import com.mcn.in4.domain.QnA.dto.QAResponseDto.*;
import com.mcn.in4.domain.QnA.dto.QARequestDto.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/qna")
@RequiredArgsConstructor
public class QAController {
    private final QAService qaService;

    @GetMapping("/")
    public List<QATitle> generateQATitleList(
            @AuthenticationPrincipal String userId){
        Long memberId = Long.parseLong(userId);
        return qaService.generateQATitleList(memberId);
    }

    @GetMapping("/detail")
    public ResponseEntity<QADetail> generateQADetail(
            @RequestParam Long qaId){
        QADetail qaDetail = qaService.generateQADetail(qaId);
        return ResponseEntity.ok(qaDetail);
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Void> uploadQA(
            @AuthenticationPrincipal String userId,
            QuestionDto request){
        Long memberId = Long.parseLong(userId);
        String questionTitle = request.getQuestionTitle();
        String questionDetail = request.getQuestionDetail();
        qaService.uploadQA(memberId, questionTitle, questionDetail);
        return  ResponseEntity.ok().build();
    }
}
