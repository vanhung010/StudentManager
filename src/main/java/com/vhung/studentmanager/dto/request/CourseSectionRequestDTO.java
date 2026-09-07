package com.vhung.studentmanager.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseSectionRequestDTO {
    @NotNull(message = "Vui lòng chọn môn học")
    private Long courseId;

    @NotNull(message = "Vui lòng chọn học kỳ")
    private Long semesterId;

    @NotNull(message = "Vui lòng chọn giảng viên")
    private Long teacherId;

    @NotNull(message = "Vui lòng nhập sĩ số tối đa")
    @Min(value = 1, message = "Sĩ số tối đa phải lớn hơn 0")
    private Integer maxStudents;
}
