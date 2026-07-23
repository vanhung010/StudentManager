package com.vhung.studentmanager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "course")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course extends BaseEntity {
    @Column(name = "course_code", unique = true, length = 50, nullable = false)
    private String courseCode;
    @Column(name = "name", length = 50, nullable = false)
    private String name;
    @Column(name = "credits", nullable = false)
    private Integer credits;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Departments department;
    @Column(name = "description", length = 200)
    private String description;

}
