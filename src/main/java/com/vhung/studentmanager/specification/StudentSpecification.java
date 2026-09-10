package com.vhung.studentmanager.specification;

import com.vhung.studentmanager.entity.CourseSections;
import com.vhung.studentmanager.entity.Enrollments;
import com.vhung.studentmanager.entity.Student;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class StudentSpecification {

    public static Specification<Student> hasStatus(String status){
        return ((root, query, criteriaBuilder) -> {
            if(!StringUtils.hasText(status)) return null;

           if(status.equalsIgnoreCase("active")){
               //trả về student active
               return  criteriaBuilder.isFalse(root.get("isDeleted"));
           }
           else {
               return criteriaBuilder.isTrue(root.get("isDeleted"));
           }
        });
    }

    public static Specification<Student> hasEnrollmentYear(Integer enrollmentYear){
    return ((root, query, criteriaBuilder) -> {
        if(enrollmentYear == null) return null;

        return criteriaBuilder.equal(root.get("enrollmentYear"), enrollmentYear);
    });
    }

    public static Specification<Student> hasDepartmentId(Long departmentId){
        return ((root, query, criteriaBuilder) -> {
            if(departmentId == null) return null;
            return criteriaBuilder.equal(root.get("departments").get("id"), departmentId);
        });
    }

    public static Specification<Student> hasKeyword(String keyword){
       return ((root, query, criteriaBuilder) -> {
           if(!StringUtils.hasText(keyword)) return null;

           String search = "%".concat(keyword.toLowerCase()).concat("%");

           return criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), search);
       });
    }

    public static Specification<Student> hasCourseSection(Long courseSectionId) {
        return (root, query, cb) -> {
            if (courseSectionId == null) {
                return null;
            }

            Join<Student, Enrollments> enrollment =
                    root.join("enrollments", JoinType.INNER);

            Join<Enrollments, CourseSections> courseSection =
                    enrollment.join("courseSection", JoinType.INNER);

            return cb.equal(
                    courseSection.get("id"),
                    courseSectionId
            );
        };
    }
}
