package com.vhung.studentmanager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admins")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admins extends BaseEntity{
    @OneToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
