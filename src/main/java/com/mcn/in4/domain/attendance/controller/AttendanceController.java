package com.mcn.in4.domain.attendance.controller;

import com.mcn.in4.domain.attendance.dto.AttendanceResponseDto;
import com.mcn.in4.domain.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<AttendanceResponseDto>> getAttendance(
            @AuthenticationPrincipal String userId,
            @org.springframework.web.bind.annotation.RequestParam(value = "startDate", required = false) String startDate,
            @org.springframework.web.bind.annotation.RequestParam(value = "endDate", required = false) String endDate,
            @org.springframework.web.bind.annotation.RequestParam(value = "status", required = false) String status) {
        Long memberId = Long.parseLong(userId);

        java.time.LocalDate start = (startDate != null && !startDate.isEmpty()) ? java.time.LocalDate.parse(startDate)
                : null;
        java.time.LocalDate end = (endDate != null && !endDate.isEmpty()) ? java.time.LocalDate.parse(endDate) : null;

        return ResponseEntity.ok(attendanceService.getAttendance(memberId, start, end, status));
    }

    @PostMapping("/check-in")
    public ResponseEntity<String> checkIn(@AuthenticationPrincipal String userId) {
        attendanceService.checkIn(Long.parseLong(userId));
        return ResponseEntity.ok("출근 처리되었습니다.");
    }

    @PostMapping("/check-out")
    public ResponseEntity<String> checkOut(@AuthenticationPrincipal String userId) {
        attendanceService.checkOut(Long.parseLong(userId));
        return ResponseEntity.ok("퇴근 처리되었습니다.");
    }
}