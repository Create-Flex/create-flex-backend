package com.mcn.in4.domain.creator.dto.response;

import com.mcn.in4.domain.creator.entity.CreatorWork;
import lombok.Builder;
import lombok.Getter;

public class CreatorWorkResponseDTO {

    @Getter
    @Builder
    public static class Info {
        private Long creatorWorkId;
        private String workName;
        private String workStatus;
        private String workerName;      // 작성자 이름

        public static Info from(CreatorWork work) {
            return Info.builder()
                    .creatorWorkId(work.getCreatorWorkId())
                    .workName(work.getWorkName())
                    .workStatus(work.getWorkStatus().name())
                    .workerName(work.getMemberWorker().getMemberName())
                    .build();
        }
    }
}
