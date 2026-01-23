package com.mcn.in4.entity.creator;

import com.mcn.in4.entity.creator.creatorEnum.WorkStatus;
import com.mcn.in4.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "creator_work") // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreatorWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creator_work_id")
    private Long creatorWorkId;
    //크리에이터 관련 업무 키

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false, foreignKey = @ForeignKey(
            foreignKeyDefinition =
                    "FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE"
    )) //상위 엔티티 삭제시 함께 삭제
    private Member memberCreator;
    //크리에이터의 사용자 키

    @Column(name = "work_name", nullable = false)
    private String workName;
    //크리에이터의 업무 제목

    @Column(name = "work_status", nullable = false)
    private WorkStatus workStatus;
    //업무 상태

    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false, foreignKey = @ForeignKey(
            foreignKeyDefinition =
                    "FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE"
    ))
    private Member memberWorker;
    //업무자의 사용자 키
}
