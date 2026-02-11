package com.mcn.in4.domain.vacation.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 내 휴가 목록 페이징 응답 DTO
 */
@Getter
@Builder
public class MyVacationPageResponseDTO {
    private List<VacationListResponseDTO> list;
    private PageInfo pageInfo;

    @Getter
    @Builder
    public static class PageInfo {
        private int currentPage;      // 현재 페이지 (1부터 시작)
        private int totalPages;       // 전체 페이지 수
        private long totalElements;   // 전체 데이터 수
        private int size;             // 페이지 크기
    }

    public static MyVacationPageResponseDTO from(Page<VacationListResponseDTO> page) {
        return MyVacationPageResponseDTO.builder()
                .list(page.getContent())
                .pageInfo(PageInfo.builder()
                        .currentPage(page.getNumber() + 1)
                        .totalPages(page.getTotalPages())
                        .totalElements(page.getTotalElements())
                        .size(page.getSize())
                        .build())
                .build();
    }
}
