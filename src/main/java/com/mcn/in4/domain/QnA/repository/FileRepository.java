package com.mcn.in4.domain.QnA.repository;

import com.mcn.in4.domain.QnA.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByQaBoard_QAId(Long qaId);
}
