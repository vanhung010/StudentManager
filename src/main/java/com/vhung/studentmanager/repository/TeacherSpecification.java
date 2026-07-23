package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.Teacher;
import org.springframework.data.jpa.domain.Specification;

public class TeacherSpecification {
    public static Specification<Teacher> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return null;
            }
            return cb.like(cb.lower(root.get("fullName")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Teacher> hasDepartmentId(Long departmentId) {
        return (root, query, cb) -> {
            if (departmentId == null) {
                return null;
            }
            return cb.equal(root.get("department").get("id"), departmentId);
        };
    }
}
