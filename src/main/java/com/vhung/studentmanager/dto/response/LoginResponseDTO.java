package com.vhung.studentmanager.dto.response;

import com.vhung.studentmanager.entity.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    private Long id;
    private String username;
    private Role role;
    private String fullName;
}