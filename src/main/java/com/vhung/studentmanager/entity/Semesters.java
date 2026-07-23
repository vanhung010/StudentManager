package com.vhung.studentmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "semesters")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Semesters extends BaseEntity {
    @Column(name = "semester_code",nullable = false, unique = true, length = 50)
    private String semesterCode;
    @Column(name = "name",nullable = false, length = 50)
    private String name;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    @Column(name = "registration_start_date", nullable = false)
    private LocalDate regStartDate;
    @Column(name = "registration_end_date", nullable = false)
    private LocalDate regEndDate;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;
}
