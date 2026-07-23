package com.vhung.studentmanager.dto.response;

import com.vhung.studentmanager.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponseDTO {
    private Long id;
    private String studentCode;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dob;
    private Gender gender;
    private int enrollmentYear;
    private String className;
    private BigDecimal gpa;
    private DepartmentResponseDTO department;
}
