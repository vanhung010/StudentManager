package com.vhung.studentmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Departments extends BaseEntity {
    @Column(name = "department_code", nullable = false)
    private String departmentCode;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "is_deleted")
    private boolean isDeleted = false;
}
