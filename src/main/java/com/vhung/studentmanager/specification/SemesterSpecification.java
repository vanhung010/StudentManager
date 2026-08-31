package com.vhung.studentmanager.specification;

import com.vhung.studentmanager.entity.Departments;
import com.vhung.studentmanager.entity.Semesters;
import org.springframework.data.jpa.domain.Specification;

public class SemesterSpecification {
    public static Specification<Semesters> hasStatus(String status){
        return ((root, query, criteriaBuilder) -> {

            if (status == null || status.isBlank() || status.equalsIgnoreCase("all")) {
                return null;
            }

            if (status.equalsIgnoreCase("active")) {
                return criteriaBuilder.isTrue(root.get("isActive"));
            }

            if (status.equalsIgnoreCase("ended")) {
                return criteriaBuilder.isFalse(root.get("isActive"));
            }
            return null;
       });

}
    public static Specification<Semesters> hasKeyword(String keyword){
        return (root, query, criteriaBuilder) -> {
            if(keyword ==  null || keyword.isBlank()){
                return null;
            }
            String patternSearch = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), patternSearch),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("semesterCode")), patternSearch));
        };
    }
}
