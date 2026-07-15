package com.vhung.studentmanager.dto.response;

import com.vhung.studentmanager.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String userName;
    private Role role;
    private boolean isActive;
    private LocalDate createdAt;
}
