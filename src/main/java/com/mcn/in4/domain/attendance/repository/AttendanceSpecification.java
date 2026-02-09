package com.mcn.in4.domain.attendance.repository;

import com.mcn.in4.domain.attendance.entity.Attendance;
import com.mcn.in4.domain.attendance.entity.attendanceEnum.CheckInStatus;
import com.mcn.in4.domain.attendance.entity.attendanceEnum.CheckOutStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttendanceSpecification {

    public static Specification<Attendance> filterAttendance(Long memberId, LocalDate startDate, LocalDate endDate,
            String status, String name) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Member ID Filter
            if (memberId != null) {
                predicates.add(criteriaBuilder.equal(root.get("member").get("memberId"), memberId));
            }

            // 2. Date Range Filter
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("attendanceDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("attendanceDate"), endDate));
            }

            // 3. Status Filter
            if (StringUtils.hasText(status)) {
                switch (status) {
                    case "NORMAL":
                        // 정상: CheckIn = NORMAL AND CheckOut = NORMAL
                        Predicate checkInNormal = criteriaBuilder.equal(root.get("checkInStatus").as(String.class),
                                "NORMAL");
                        Predicate checkOutNormal = criteriaBuilder.equal(root.get("checkOutStatus").as(String.class),
                                "NORMAL");
                        predicates.add(criteriaBuilder.and(checkInNormal, checkOutNormal));
                        break;
                    case "WORKING":
                        // 근무중: CheckIn != NULL AND CheckOut = NULL
                        Predicate checkInNotNull = criteriaBuilder.isNotNull(root.get("checkInStatus"));
                        Predicate checkOutNull = criteriaBuilder.isNull(root.get("checkOutStatus"));
                        predicates.add(criteriaBuilder.and(checkInNotNull, checkOutNull));
                        break;
                    case "LATE":
                        // 지각: CheckIn = LATE
                        predicates.add(criteriaBuilder.equal(root.get("checkInStatus").as(String.class), "LATE"));
                        break;
                    case "EARLY_LEAVE":
                        // 조퇴: CheckOut = EARLY_LEAVE
                        predicates
                                .add(criteriaBuilder.equal(root.get("checkOutStatus").as(String.class), "EARLY_LEAVE"));
                        break;
                    case "OVERTIME":
                        // 초과: CheckOut = OVERTIME
                        predicates.add(criteriaBuilder.equal(root.get("checkOutStatus").as(String.class), "OVERTIME"));
                        break;
                    case "VACATION":
                        // 휴가: CheckIn = VACATION
                        predicates.add(criteriaBuilder.equal(root.get("checkInStatus").as(String.class), "VACATION"));
                        break;
                    case "ABSENT":
                        // 결근: CheckIn = ABSENT
                        predicates.add(criteriaBuilder.equal(root.get("checkInStatus").as(String.class), "ABSENT"));
                        break;
                    case "HALF_VACATION":
                        // 반차: CheckIn = HALF_VACATION OR CheckOut = HALF_VACATION
                        Predicate checkInHalf = criteriaBuilder.equal(root.get("checkInStatus").as(String.class),
                                "HALF_VACATION");
                        Predicate checkOutHalf = criteriaBuilder.equal(root.get("checkOutStatus").as(String.class),
                                "HALF_VACATION");
                        predicates.add(criteriaBuilder.or(checkInHalf, checkOutHalf));
                        break;
                    case "WORKATION":
                        // 워케이션: CheckIn = WORKATION OR CheckOut = WORKATION
                        Predicate checkInWork = criteriaBuilder.equal(root.get("checkInStatus").as(String.class),
                                "WORKATION");
                        Predicate checkOutWork = criteriaBuilder.equal(root.get("checkOutStatus").as(String.class),
                                "WORKATION");
                        predicates.add(criteriaBuilder.or(checkInWork, checkOutWork));
                        break;
                    default:
                        // 그 외 상태: 문자열 매칭 (Fallback)
                        Predicate checkInMatch = criteriaBuilder.equal(root.get("checkInStatus").as(String.class),
                                status);
                        Predicate checkOutMatch = criteriaBuilder.equal(root.get("checkOutStatus").as(String.class),
                                status);
                        predicates.add(criteriaBuilder.or(checkInMatch, checkOutMatch));
                        break;
                }
            }

            // 4. Name Filter (Search)
            if (StringUtils.hasText(name)) {
                predicates.add(criteriaBuilder.like(root.get("member").get("memberName"), "%" + name + "%"));
            }

            // Order by attendanceDate DESC default
            query.orderBy(criteriaBuilder.desc(root.get("attendanceDate")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
