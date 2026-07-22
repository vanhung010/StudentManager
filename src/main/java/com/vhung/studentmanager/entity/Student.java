package com.vhung.studentmanager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@Table(name = "student")
@NoArgsConstructor
@AllArgsConstructor
public class Student extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Departments departments;
    @Column(name = "student_code", nullable = false, unique = true, length = 20)
    private String studentCode;
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "phone", length = 15)
    private String phone;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;
    @Column(name = "enrollment_year", nullable = false)
    private Integer enrollmentYear;
    @Column(name = "class_name", length = 20)
    private String className;
    @Column(name = "gpa", precision = 3, scale = 2)
    private BigDecimal gpa;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
