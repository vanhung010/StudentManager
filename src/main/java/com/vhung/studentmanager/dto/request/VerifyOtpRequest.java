package com.vhung.studentmanager.dto.request;

import lombok.Data;

@Data

public class VerifyOtpRequest {
    private String identifier;
    private String otpCode;
}
