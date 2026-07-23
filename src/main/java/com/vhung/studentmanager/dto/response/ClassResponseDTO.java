package com.vhung.studentmanager.dto.response;

import com.vhung.studentmanager.entity.Departments;
import com.vhung.studentmanager.entity.Teacher;
import lombok.Builder;

@Builder
public class ClassResponseDTO {
    private Long id;
    private String classCode;
    private String name;
    private Departments department;
    private Teacher advisor;
    private Integer enrollmentYear;
}
