package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseReposistory extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course>{

    boolean existsByCourseCode(String courseCode);

    boolean existsByCourseCodeAndIdNot(String courseCode, Long id);

    Optional<Course> findByIdAndIsDeletedFalse(Long id);

    Optional<Course> findByIdAndIsDeletedTrue(Long id);
}
