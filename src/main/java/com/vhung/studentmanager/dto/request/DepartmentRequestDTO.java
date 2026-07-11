package com.vhung.studentmanager.dto.request;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequestDTO {
    private String departmentCode;
    private String name;
}
