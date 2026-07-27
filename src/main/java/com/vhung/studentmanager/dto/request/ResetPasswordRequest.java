package com.vhung.studentmanager.dto.request;

import lombok.Data;

@Data

public class ResetPasswordRequest {
    private String identifier;
    private String otpCode;
    private String newPassword;
}
