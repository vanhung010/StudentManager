package com.vhung.studentmanager.dto.request;

import lombok.Data;

@Data
public class CourseRequestDTO {
    private String courseCode;
    private Integer credits;
    private String name;
    private Long idDepartment;
    private String description;
}
