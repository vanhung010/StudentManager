package com.vhung.studentmanager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassRequestDTO {
    private String classCode;
    private String name;
    private Long departmentId;
    private Long advisorId;
    private Integer enrollmentYear;
}
