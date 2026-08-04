package com.vhung.studentmanager.specification;

import com.vhung.studentmanager.entity.Classes;
import com.vhung.studentmanager.entity.Departments;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class DepartmentSpecification  {
    public static Specification<Departments> hasStatus(String status){

        return ((root, query, criteriaBuilder) -> {

            if(status == null || status.isBlank() || status.equalsIgnoreCase("all")){
                return null;
            }

            if(status.equalsIgnoreCase("active")){
                return criteriaBuilder.isFalse(root.get("isDeleted"));
            }

            if(status.equalsIgnoreCase("deleted")){
                return criteriaBuilder.isTrue(root.get("isDeleted"));
            }
            return null;
        });
    }

    public static Specification<Departments> hasKeyword(String keyword){
        return (root, query, criteriaBuilder) -> {
            if(keyword ==  null || keyword.isBlank()){
                return null;
            }
            String patternSearch = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), patternSearch ),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("departmentCode")), patternSearch));
        };
    }

    public static Specification<Departments> hasIdClass(Long id){
        return (root, query, criteriaBuilder) -> {
            if(id == null) return null;
            Join<Departments, Classes> classJoin = root.join("classes");
            return criteriaBuilder.equal(classJoin.get("id"), id);
        };
    }
}
