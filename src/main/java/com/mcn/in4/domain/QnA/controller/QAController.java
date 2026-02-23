package com.mcn.in4.domain.QnA.controller;

import com.mcn.in4.domain.QnA.service.QAService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.mcn.in4.domain.QnA.dto.QAResponseDto.*;
import com.mcn.in4.domain.QnA.dto.QARequestDto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/qna")
@RequiredArgsConstructor
public class QAController {
    private final QAService qaService;

    @GetMapping("/")
    public List<QATitle> generateQATitleList(
            @AuthenticationPrincipal String userId,
            @RequestParam Long listPage){
        Long memberId = Long.parseLong(userId);
        return qaService.generateQATitleList(memberId, listPage);
    }

    @GetMapping("/detail")
    public ResponseEntity<QADetail> generateQADetail(
            @RequestParam Long qaId){
        QADetail qaDetail = qaService.generateQADetail(qaId);
        return ResponseEntity.ok(qaDetail);
    }

    @PostMapping("/question")
    @ResponseBody
    public QAFileUpload uploadQuestion(
            @AuthenticationPrincipal String userId,
            @ModelAttribute QuestionDto request){
        Long memberId = Long.parseLong(userId);
        String questionTitle = request.getQuestionTitle();
        String questionDetail = request.getQuestionDetail();
        List<MultipartFile> files = request.getFiles();
        return qaService.uploadQuestion(memberId, questionTitle, questionDetail, files);
    }

    @PostMapping("/answer")
    @ResponseBody
    public ResponseEntity<Void> uploadAnswer(
            @AuthenticationPrincipal String userId,
            @RequestBody AnswerDto request){
        Long memberId = Long.parseLong(userId);
        Long qaId = request.getQaId();
        String answerDetail = request.getAnswerDetail();
        qaService.uploadAnswer(memberId, qaId, answerDetail);
        return  ResponseEntity.ok().build();
    }
}
