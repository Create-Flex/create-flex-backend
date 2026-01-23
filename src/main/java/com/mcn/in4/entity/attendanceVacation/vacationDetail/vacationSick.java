package com.mcn.in4.entity.attendanceVacation.vacationDetail;

import com.mcn.in4.entity.attendanceVacation.Vacation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vacation_sick") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class vacationSick {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sick_id")
    private Long sickId;
    //병가 키

    @ManyToOne
    @JoinColumn(name = "vacation_id", nullable = false, foreignKey = @ForeignKey(
            foreignKeyDefinition =
                    "FOREIGN KEY (vacation_id) REFERENCES vacation(id) ON DELETE CASCADE"
    ))
    private Vacation vacation;
    //휴가 키

    @Column(name = "sick_detail", nullable = false)
    private String sickDetail;
    //증상 상세
}
