package com.vhung.studentmanager.specification;

import com.vhung.studentmanager.entity.CourseSections;
import com.vhung.studentmanager.entity.enums.SectionStatus;
import org.springframework.data.jpa.domain.Specification;

public class CourseSectionSpecification {

    public static Specification<CourseSections> hasSemester(Long semesterId) {
        return (root, query, cb) -> {
            if (semesterId == null) return null;
            return cb.equal(root.get("semester").get("id"), semesterId);
        };
    }

    public static Specification<CourseSections> hasCourse(Long courseId) {
        return (root, query, cb) -> {
            if (courseId == null) return null;
            return cb.equal(root.get("course").get("id"), courseId);
        };
    }

    public static Specification<CourseSections> hasTeacher(Long teacherId) {
        return (root, query, cb) -> {
            if (teacherId == null) return null;
            return cb.equal(root.get("teacher").get("id"), teacherId);
        };
    }

    public static Specification<CourseSections> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isBlank() || status.equalsIgnoreCase("all")) return null;
            try {
                SectionStatus sectionStatus = SectionStatus.valueOf(status.toUpperCase());
                return cb.equal(root.get("status"), sectionStatus);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }
}
