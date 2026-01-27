package com.mcn.in4.domain.vacation.repository;

import com.mcn.in4.domain.vacation.entity.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacationRepository extends JpaRepository<Vacation, Long> {

    List<Vacation> findByMemberMemberIdOrderByVacationRequestDesc(Long memberId);
}
