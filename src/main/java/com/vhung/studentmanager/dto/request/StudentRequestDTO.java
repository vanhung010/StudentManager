package com.vhung.studentmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentRequestDTO {
    @NotBlank(message = "Mã sinh viên không được để trống")
    private String studentCode;
    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;
    @NotBlank(message = "Email không được để trống")
    private String email;
    private String phone;
    private LocalDate dob;
    private String gender;
    @NotNull(message = "Khoa không được để trống")
    private Long departmentId;
    @NotNull(message = "Năm nhập học không được để trống")
    private Integer enrollmentYear;

    private Long classId;

    @NotBlank(message = "Username không được để trống")
    private String username;
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

}
