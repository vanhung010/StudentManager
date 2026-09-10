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
    private String departmentCode;
    private Long departmentId;
    private String userName;
    private String phone_number;
    private Boolean isDeleted;
    private String departmentName;


    public static TeacherResponseDTO fromEntity(Teacher teacher) {
        TeacherResponseDTO dto = new TeacherResponseDTO();
        dto .setId(teacher.getId());
        dto.setTeacherCode(teacher.getTeacherCode());
        dto.setFullName(teacher.getFullName());
        dto.setEmail(teacher.getEmail());
        dto.setPhone_number(teacher.getPhoneNumber());
        dto.setIsDeleted(teacher.getIsDeleted());
        dto.setDepartmentName(teacher.getDepartment().getName());

        if (teacher.getUser() != null) {
            dto.setUserName(teacher.getUser().getUserName());
        }

        if (teacher.getDepartment() != null) {
            dto.setDepartmentId(teacher.getDepartment().getId());
            dto.setDepartmentCode(teacher.getDepartment().getDepartmentCode());
        }
        return dto;
    }
}
