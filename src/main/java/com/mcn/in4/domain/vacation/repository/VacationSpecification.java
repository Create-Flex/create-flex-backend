package com.mcn.in4.domain.vacation.repository;

import com.mcn.in4.domain.vacation.entity.Vacation;
import com.mcn.in4.domain.vacation.entity.enums.VacationApprove;
import com.mcn.in4.domain.vacation.entity.enums.VacationType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 휴가 조회 Specification (동적 쿼리)
 */
public class VacationSpecification {

    public static Specification<Vacation> filterVacation(
            LocalDate startDate,
            LocalDate endDate,
            VacationApprove status,
            String name,
            VacationType type
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. 휴가 시작일 기준 기간 필터
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("vacationStart"), startDate));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("vacationStart"), endDate));
            }

            // 2. 승인 상태 필터
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("vacationApprove"), status));
            }

            // 3. 이름 검색 필터
            if (StringUtils.hasText(name)) {
                predicates.add(criteriaBuilder.like(root.get("member").get("memberName"), "%" + name + "%"));
            }

            // 4. 휴가 유형 필터
            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get("vacationType"), type));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
