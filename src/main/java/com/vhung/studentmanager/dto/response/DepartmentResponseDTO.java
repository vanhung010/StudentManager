package com.vhung.studentmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class DepartmentResponseDTO {
    private Long id;
    private String name;
    private String departmentCode;
}
