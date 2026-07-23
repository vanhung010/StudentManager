package com.vhung.studentmanager.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentRequest {
    private String fullName;
    private String email;
    private String phone;
    private String className;
}
