package com.vhung.studentmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class TeacherRequestDTO {
    @NotBlank(message = "Mã giáo viên không được để trống")
    private String teacherCode;
    @NotBlank(message = "Tên giáo viên không được để trống")
    private String fullName;
    @NotBlank(message = "Email không được để trống")
    private String email;
    @NotBlank(message = "Số điện thoại không được để trống")
    private String phoneNumber;
    @NotBlank(message = "Vui lòng chọn khoa")
    private Long idDepartment;
    @NotBlank(message = "Username không được để trống")
    private String userName;
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}
