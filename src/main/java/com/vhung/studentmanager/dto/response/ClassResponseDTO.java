package com.vhung.studentmanager.dto.response;

import lombok.Builder;

@Builder
public class ClassResponseDTO {
    private Long id;
    private String classCode;
    private String name;
}
