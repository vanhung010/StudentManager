package com.vhung.studentmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
@Data
public class SemesterRequestDTO {
    @NotBlank(message = "Mã học kỳ không được để trống")
    private String semesterCode;

    @NotBlank(message = "Tên học kỳ không được để trống")
    private String name;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate endDate;

    @NotNull(message = "Ngày mở đăng ký không được để trống")
    private LocalDate regStartDate;

    @NotNull(message = "Ngày đóng đăng ký không được để trống")
    private LocalDate regEndDate;

    // Ứng với toggle "Đặt làm học kỳ hiện tại"
    private Boolean setAsCurrent = false;
}
