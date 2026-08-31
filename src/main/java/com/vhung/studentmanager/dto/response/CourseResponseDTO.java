package com.vhung.studentmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDTO {
    private Long id;
    private String courseCode;
    private String name;
    private Integer credits;
    private Long departmentId;
    private String departmentName;
    private String description;
    private Boolean isDeleted;
}
