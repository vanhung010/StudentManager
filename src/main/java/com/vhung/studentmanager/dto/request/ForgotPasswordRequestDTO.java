package com.vhung.studentmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ForgotPasswordRequestDTO {
    @NotBlank(message = "Vui lòng nhập tên đăng nhập")
    private String identifier;

    @NotBlank(message = "Vui lòng nhập mã OTP")
    private String otpCode;

    @NotBlank(message = "Vui lòng nhập mật khẩu mới")
    @Size(min = 8, message = "Mật khẩu mới tối thiểu 8 ký tự")
    private String newPassword;
}
