package com.mcn.in4.domain.schedule.repository;

import com.mcn.in4.domain.schedule.entity.Schedule;
import com.mcn.in4.domain.schedule.entity.scheduleEnum.ScheduleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SchedulRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s " +
            "WHERE s.member.memberId = :memberId " +
            "AND s.scheduleDate BETWEEN :startDate AND :endDate " +
            "AND s.scheduleType IN :types")
    List<Schedule> findMyMonthlySchedules(
            @Param("memberId") Long memberId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("types") List<ScheduleType> types);

    @Query("SELECT s FROM Schedule s " +
            "LEFT JOIN FETCH s.creator " +
            "WHERE (s.member.memberId = :memberId " + // 본인이 작성한 일정
            "OR s.creator.memberId = :memberId " +    // (크리에이터용) 본인이 대상인 일정
            "OR s.creator.memberId IN :managedCreatorIds) " + // (매니저용) 관리 중인 크리에이터들의 일정
            "AND s.scheduleDate BETWEEN :startDate AND :endDate " +
            "AND s.scheduleType IN :types")
    List<Schedule> findCreatorRelatedSchedules( //추후 인덱스 구현하여 조회성능 향상 예정
            @Param("memberId") Long memberId,
            @Param("managedCreatorIds") List<Long> managedCreatorIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("types") List<ScheduleType> types);

}
