package com.vhung.studentmanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    @OneToMany(mappedBy = "department")
    private List<Classes> classes;

}
