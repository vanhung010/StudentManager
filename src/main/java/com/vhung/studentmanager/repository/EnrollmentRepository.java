package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.Enrollments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface EnrollmentRepository extends JpaRepository<Enrollments, Long> {
    int countByCourseSection_Id(Long courseSectionId);
}
