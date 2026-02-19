package com.mcn.in4.domain.QnA.service;

import com.mcn.in4.domain.QnA.entity.QABoard;
import com.mcn.in4.domain.QnA.repository.QARepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.MemberEmployeeDetail;
import com.mcn.in4.domain.member.repository.MemberEmployeeDetailRepository;
import com.mcn.in4.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mcn.in4.domain.QnA.dto.QAResponseDto.*;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class QAServiceImpl implements QAService{
    private final QARepository qaRepository;
    private final MemberRepository memberRepository;
    private final MemberEmployeeDetailRepository memberEmployeeDetailRepository;
    private final MailService mailService;

    @Override
    public List<QATitle> generateQATitleList(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow();
        List<QABoard> qaBoards = (member.getMemberRole() == MemberRole.ADMINISTRATOR)
            ? qaRepository.findAllByOrderByQuestionTimeDesc()
                : qaRepository.findByQuestionMember_MemberIdOrderByQuestionTimeDesc(memberId);

        return qaBoards.stream().map(qaBoard -> QATitle.builder()
                        .qaId(qaBoard.getQAId())
                        .questionTitle(qaBoard.getQuestionTitle())
                        .questionTime(qaBoard.getQuestionTime())
                        .questionMemberName(qaBoard.getQuestionMember().getMemberName())
                        .departmentName(qaBoard.getQuestionMember().getDepartment() != null
                                ? qaBoard.getQuestionMember().getDepartment().getDepartmentName()
                                : "부서없음")
                        .answered(qaBoard.getAnswerTime() != null)
                        .build()
                ).toList();
    }

    @Override
    public QADetail generateQADetail(Long qaId){
        QABoard qaBoard = qaRepository.findByQAId(qaId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 문의입니다."));

        return QADetail.builder()
                .qaId(qaBoard.getQAId())
                .questionTitle(qaBoard.getQuestionTitle())
                .questionTime(qaBoard.getQuestionTime())
                .questionMemberName(qaBoard.getQuestionMember().getMemberName())
                .questionDepartmentName(qaBoard.getQuestionMember().getDepartment() != null
                        ? qaBoard.getQuestionMember().getDepartment().getDepartmentName()
                        : "부서없음")
                .questionDetail(qaBoard.getQuestionDetail())
                .answered(qaBoard.getAnswerTime() != null)
                .answerTime(qaBoard.getAnswerTime())
                .answerMemberName(qaBoard.getAnswerMember() != null
                        ? qaBoard.getAnswerMember().getMemberName()
                        : null)
                .answerDepartmentName(qaBoard.getAnswerMember() != null
                        ? qaBoard.getAnswerMember().getDepartment().getDepartmentName()
                        :null)
                .answerDetail(qaBoard.getAnswerDetail())
                .build();
    }

    @Override
    public void uploadQuestion(Long memberId, String questionTitle, String questionDetail){
        Member questionMember = memberRepository.findById(memberId).orElseThrow();

        QABoard qaBoard = QABoard.builder()
                .questionMember(questionMember)
                .questionTitle(questionTitle)
                .questionDetail(questionDetail)
                .questionTime(LocalDateTime.now())
                .build();

        mailService.sendUpQuest(questionTitle, questionMember.getMemberName(), questionMember.getDepartment().getDepartmentName(), questionDetail);
        qaRepository.save(qaBoard);
    }

    @Override
    public void uploadAnswer(Long memberId, Long qaId, String answerDetail){
        Member answerMember = memberRepository.findById(memberId).orElseThrow();
        QABoard qaBoard = qaRepository.findByQAId(qaId).orElseThrow();
        MemberEmployeeDetail questionMemberDetail = memberEmployeeDetailRepository.findByMemberMemberId(qaBoard.getQuestionMember().getMemberId()).orElseThrow();

        mailService.sendUpAnswer(questionMemberDetail.getPersonalEmail(), qaBoard.getQuestionTitle(),
                                answerMember.getMemberName(), answerMember.getDepartment().getDepartmentName(),
                                qaBoard.getQuestionTitle(), answerDetail);

        qaBoard.setAnswerMember(answerMember);
        qaBoard.setAnswerDetail(answerDetail);
        qaBoard.setAnswerTime(LocalDateTime.now());
    }
}
