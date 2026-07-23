package com.vhung.studentmanager.dto.response;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder

public class PageResponse<T> {
    private List<T> content;
    private long totalElement;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public static <T> PageResponse<T> from(Page<T> page){
        return PageResponse.<T>builder()
                .content(page.getContent())
                .totalElement(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .build();
    }
}