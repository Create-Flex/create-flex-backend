package com.mcn.in4.domain.health.dto;

import com.mcn.in4.domain.health.entity.CheckupSummanary;
import com.mcn.in4.domain.health.entity.Health;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class HealthResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HealthInfo{
        private Long healthIO;
        private String checkupName; //검진명
        private CheckupSummanary checkupSummanary; //검진요약
        private LocalDate checkupDate; //검진일
        private String checkupFileUrl; //검진지 조회 Url

        public static HealthInfo from(Health health){
            return new HealthInfo(
                    health.getHealthId(),
                    health.getCheckupName(),
                    health.getCheckupSummanary(),
                    health.getCheckupDate(),
                    health.getCheckupFileUrl()
            );
        }

        public static HealthInfo from(Health health, String memberName){
            return new HealthInfo(
                    health.getHealthId(),
                    memberName,
                    health.getCheckupSummanary(),
                    health.getCheckupDate(),
                    health.getCheckupFileUrl()
            );
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreatorHealthInfo{
        private List<HealthSummanaryCountDto> healthSummanaryCountList;
        private List<HealthInfo> healthInfoList;
        private List<MentalHealthDto> mentalHealthInfoList;
        private List<MentalHealthDto> mentalWarningHealthInfoList;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MypageHealthInfo{
        private List<HealthInfo> healthInfoList;
        private boolean haveHealthChecked;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HealthPresigned{
        private String presignedUrl; //업로드용 프리사인드 Url
    }
}