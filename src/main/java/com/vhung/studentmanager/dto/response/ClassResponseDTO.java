package com.vhung.studentmanager.dto.response;

import com.vhung.studentmanager.entity.Departments;
import com.vhung.studentmanager.entity.Teacher;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassResponseDTO {
    private Long id;
    private String classCode;
    private String name;
    private String departmentCode;
    private String fullNameTeacher;
    private Integer totalStudents;
    private Integer enrollmentYear;
}
