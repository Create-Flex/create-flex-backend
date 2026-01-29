package com.mcn.in4.domain.attendance.controller;

import com.mcn.in4.domain.attendance.dto.AttendanceResponseDto;
import com.mcn.in4.domain.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<AttendanceResponseDto>> getAttendance(
            @AuthenticationPrincipal String userId,
            org.springframework.security.core.Authentication authentication,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "status", required = false) String status) {
        log.info("여기 들어옴 : getAttendance");

        boolean isCreator = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CREATOR"));

        if (isCreator) {
            return ResponseEntity.status(403).build();
        }

        Long memberId = Long.parseLong(userId);

        java.time.LocalDate start = (startDate != null && !startDate.isEmpty()) ? java.time.LocalDate.parse(startDate)
                : null;
        java.time.LocalDate end = (endDate != null && !endDate.isEmpty()) ? java.time.LocalDate.parse(endDate) : null;

        return ResponseEntity.ok(attendanceService.getAttendance(memberId, start, end, status));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AttendanceResponseDto>> getAllAttendance(
            @AuthenticationPrincipal String userId,
            org.springframework.security.core.Authentication authentication,
            @org.springframework.web.bind.annotation.RequestParam(value = "startDate", required = false) String startDate,
            @org.springframework.web.bind.annotation.RequestParam(value = "endDate", required = false) String endDate,
            @org.springframework.web.bind.annotation.RequestParam(value = "status", required = false) String status) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRATOR"));

        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        java.time.LocalDate start = (startDate != null && !startDate.isEmpty()) ? java.time.LocalDate.parse(startDate)
                : null;
        java.time.LocalDate end = (endDate != null && !endDate.isEmpty()) ? java.time.LocalDate.parse(endDate) : null;

        return ResponseEntity.ok(attendanceService.getAllAttendance(start, end, status));
    }

    @PostMapping("/check-in")
    public ResponseEntity<String> checkIn(@AuthenticationPrincipal String userId,
            org.springframework.security.core.Authentication authentication) {
        boolean isCreator = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CREATOR"));

        if (isCreator) {
            return ResponseEntity.status(403).body("크리에이터는 출근 처리를 할 수 없습니다.");
        }

        attendanceService.checkIn(Long.parseLong(userId));
        return ResponseEntity.ok("출근 처리되었습니다.");
    }

    @PostMapping("/check-out")
    public ResponseEntity<String> checkOut(@AuthenticationPrincipal String userId,
            org.springframework.security.core.Authentication authentication) {
        boolean isCreator = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CREATOR"));

        if (isCreator) {
            return ResponseEntity.status(403).body("크리에이터는 퇴근 처리를 할 수 없습니다.");
        }

        attendanceService.checkOut(Long.parseLong(userId));
        return ResponseEntity.ok("퇴근 처리되었습니다.");
    }
}