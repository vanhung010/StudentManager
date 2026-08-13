package com.vhung.studentmanager.specification;

import com.vhung.studentmanager.entity.Course;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecification {
    private CourseSpecification() {
    }

    // THÊM: lọc trạng thái active / deleted / all.
    public static Specification<Course> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.isBlank() || status.equalsIgnoreCase("all")) {
                return null;
            }

            if (status.equalsIgnoreCase("active")) {
                return criteriaBuilder.isFalse(root.get("isDeleted"));
            }

            if (status.equalsIgnoreCase("deleted")) {
                return criteriaBuilder.isTrue(root.get("isDeleted"));
            }

            return null;
        };
    }

    // THÊM: tìm theo mã hoặc tên môn học.
    public static Specification<Course> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("courseCode")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern)
            );
        };
    }

    // THÊM: lọc theo khoa.
    public static Specification<Course> hasDepartmentId(Long departmentId) {
        return (root, query, criteriaBuilder) -> {
            if (departmentId == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get("department").get("id"),
                    departmentId
            );
        };
    }
}

