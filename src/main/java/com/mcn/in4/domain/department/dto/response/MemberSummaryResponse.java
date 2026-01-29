package com.mcn.in4.domain.department.dto.response;

import com.mcn.in4.domain.attendance.entity.Attendance;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.MemberEmployeeDetail;
import com.mcn.in4.domain.attendance.entity.attendanceEnum.AttendanceStatus;
import lombok.Getter;

@Getter
public class MemberSummaryResponse {
    private final Long memberId;
    private final String memberName;
    private final String engName;
    private final String task;
    private final String attendanceStatus; // 추가: 근무 상태 설명 (예: "근무중")

    public MemberSummaryResponse(Member member, MemberEmployeeDetail detail, Attendance attendance) {
        this.memberId = member.getMemberId();
        this.memberName = member.getMemberName();
        this.task = member.getTask();
        this.engName = (detail != null) ? detail.getEngName() : null;

        // 1. 근태 기록(attendance)이 아예 없는 경우 -> "미출근"
        if (attendance == null) {
            this.attendanceStatus = "미출근";
        } else {
            // 2. 기록이 있다면 Enum에 정의된 description("근무중", "지각", "퇴근" 등)을 가져옴
            this.attendanceStatus = attendance.getAttendanceStatus().getDescription();
        }
    }
}