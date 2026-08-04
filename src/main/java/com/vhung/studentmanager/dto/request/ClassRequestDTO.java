package com.vhung.studentmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassRequestDTO {
    @NotBlank(message = "Mã lớp không được để trống")
    private String classCode;
    @NotBlank(message = "Tên lớp không được để trống")
    private String name;
    private Long departmentId;
    private Long advisorId;
    @NotNull(message = "Năm nhập học không được để trống")
    private Integer enrollmentYear;
}
