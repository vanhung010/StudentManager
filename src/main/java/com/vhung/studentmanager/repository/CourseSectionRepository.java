package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.CourseSections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSections, Long>, JpaSpecificationExecutor<CourseSections> {
    boolean existsByCourse_IdAndSemester_IdAndTeacher_Id(Long courseId, Long semesterId, Long teacherId);

    long countByCourse_IdAndSemester_Id(Long courseId, Long semesterId);

    boolean existsBySectionCode(String sectionCode);
    
}
