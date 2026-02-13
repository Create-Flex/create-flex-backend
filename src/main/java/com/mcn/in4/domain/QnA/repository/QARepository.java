package com.mcn.in4.domain.QnA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mcn.in4.domain.QnA.entity.QABoard;

import java.util.List;
import java.util.Optional;

@Repository
public interface QARepository extends JpaRepository<QABoard, Long> {
    List<QABoard> findByQuestionMember_MemberIdOrderByQuestionTimeDesc(Long memberId);
    List<QABoard> findAllByOrderByQuestionTimeDesc();
    Optional<QABoard> findByQAId(Long qaId);
}
