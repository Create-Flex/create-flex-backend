package com.mcn.in4.domain.QnA.service;

import com.mcn.in4.domain.QnA.entity.QABoard;
import com.mcn.in4.domain.QnA.repository.QARepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mcn.in4.domain.QnA.dto.QAResponseDto.*;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class QAServiceImpl implements QAService{
    private final QARepository qaRepository;
    private final MemberRepository memberRepository;

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
                .questionDetail(qaBoard.getQuestionDetail())
                .answered(qaBoard.getAnswerTime() != null)
                .answerTime(qaBoard.getAnswerTime())
                .answerMemberName(qaBoard.getAnswerMember() != null
                        ? qaBoard.getAnswerMember().getMemberName()
                        : null)
                .answerDetail(qaBoard.getAnswerDetail())
                .build();
    }
}
