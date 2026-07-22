package com.vhung.studentmanager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DepartmentResponseDTO {
    private Long id;
    private String name;
    private String departmentCode;
}
