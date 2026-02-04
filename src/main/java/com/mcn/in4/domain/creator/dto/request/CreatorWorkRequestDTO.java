package com.mcn.in4.domain.creator.dto.request;

import lombok.Getter;

public class CreatorWorkRequestDTO {

    @Getter
    public static class Create {
        private String workName;
    }

    @Getter
    public static class UpdateStatus {
        private String workStatus;
    }
}
