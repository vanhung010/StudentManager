package com.vhung.studentmanager.entity;

import com.vhung.studentmanager.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    @Column(name = "user_name", nullable = false, unique = true, length = 50)
    private String userName;
    @Column(name = "password", nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
}
