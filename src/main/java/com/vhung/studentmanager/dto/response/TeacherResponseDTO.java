package com.vhung.studentmanager.dto.response;

import com.vhung.studentmanager.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherResponseDTO {
    private Long id;
    private String teacherCode;
    private String fullName;
    private String email;
    private String departmentName;
    private Long departmentId;
    private String userName;


    public static TeacherResponseDTO fromEntity(Teacher teacher) {
        TeacherResponseDTO dto = new TeacherResponseDTO();
        dto .setId(teacher.getId());
        dto.setTeacherCode(teacher.getTeacherCode());
        dto.setFullName(teacher.getFullName());
        dto.setEmail(teacher.getEmail());

        if (teacher.getUser() != null) {
            dto.setUserName(teacher.getUser().getUserName());
        }

        if (teacher.getDepartment() != null) {
            dto.setDepartmentId(teacher.getDepartment().getId());
            dto.setDepartmentName(teacher.getDepartment().getName());
        }
        return dto;
    }
}
