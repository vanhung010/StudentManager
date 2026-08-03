package com.vhung.studentmanager.specification;

import com.vhung.studentmanager.entity.Classes;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ClassSpecification {

    public static Specification<Classes> hasIdDepartment(Long idDepartment){
        return ((root, query, criteriaBuilder) -> {
            if(idDepartment == null ){
                return null;
            }
            else {
                return criteriaBuilder.equal(root.get("department").get("id"), idDepartment);
            }
        });
    }

    public static Specification<Classes> hasEnrollmentYear(Integer enrollmentYear){
        return ((root, query, criteriaBuilder) -> {
            if(enrollmentYear == null) return null;

            return criteriaBuilder.equal(root.get("enrollmentYear"), enrollmentYear);
        });
    }

    public static Specification<Classes> hasStatus(String status){
        return ((root, query, criteriaBuilder) -> {
        if(!StringUtils.hasText(status)){
            return null;
        }
        else if(status.equalsIgnoreCase("active")){
            return criteriaBuilder.isFalse(root.get("isDeleted"));
        }
        else {
            return criteriaBuilder.isTrue(root.get("isDeleted"));
        }
        });
    }

    public static Specification<Classes> hasKeyword(String keyword){
        return ((root, query, criteriaBuilder) -> {
            if(!StringUtils.hasText(keyword)){
                return null;
            }
            else {
                String search = "%".concat(keyword.toLowerCase()).concat("%");
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), search),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("classCode")), search));
            }
        });
    }
}
