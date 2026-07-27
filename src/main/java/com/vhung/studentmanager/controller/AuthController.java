package com.vhung.studentmanager.controller;

import com.vhung.studentmanager.dto.request.ForgotPasswordRequestDTO;
import com.vhung.studentmanager.dto.request.LoginRequestDTO;
import com.vhung.studentmanager.dto.request.ResetPasswordRequest;
import com.vhung.studentmanager.dto.request.VerifyOtpRequest;
import com.vhung.studentmanager.dto.response.ApiResponse;
import com.vhung.studentmanager.dto.response.LoginResponseDTO;
import com.vhung.studentmanager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO){
        LoginResponseDTO data = authService.login(loginRequestDTO);

        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDTO request) {
        authService.sendResetOtp(request.getIdentifier());
        return ApiResponse.ok("Đã gửi mã OTP đến email của bạn", null);
    }

    @PostMapping("/verify-otp")
    public ApiResponse<Void> verifyOtp(@RequestBody @Valid VerifyOtpRequest request){
        authService.verifyOtp(request.getIdentifier(), request.getOtpCode());
        return ApiResponse.ok("Mã OTP hợp lệ", null);
    }
    @PutMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authService.resetPassword(request.getIdentifier(), request.getOtpCode(), request.getNewPassword());
        return ApiResponse.ok("Đặt lại mật khẩu thành công", null);
    }
}
