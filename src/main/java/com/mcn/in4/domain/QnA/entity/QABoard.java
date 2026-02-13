package com.mcn.in4.domain.QnA.entity;

import com.mcn.in4.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "QA_board")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class QABoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QA_id")
    private Long QAId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_member", nullable = false)
    private Member questionMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_member", nullable = true)
    private Member answerMember;

    @Column(name = "question_title", nullable = false)
    private String questionTitle;

    @Column(name = "question_detail", nullable = false)
    private String questionDetail;

    @Column(name = "answer_detail", nullable = true)
    private String answerDetail;

    @Column(name = "question_time", nullable = false)
    private LocalDateTime questionTime;

    @Column(name = "answer_time", nullable = true)
    private LocalDateTime answerTime;
}
