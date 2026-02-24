package com.mcn.in4.domain.QnA.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mcn.in4.domain.QnA.entity.QABoard;

import java.util.List;
import java.util.Optional;

@Repository
public interface QARepository extends JpaRepository<QABoard, Long> {
    Page<QABoard> findByQuestionMember_MemberIdOrderByQuestionTimeDesc(Long memberId, Pageable pageRequest);
    Page<QABoard> findAllByOrderByQuestionTimeDesc(Pageable pageRequest);
    Optional<QABoard> findByQAId(Long qaId);
}
