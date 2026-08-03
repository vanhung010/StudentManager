package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<Classes, Long>, JpaSpecificationExecutor<Classes> {

    Optional<Classes> findByIdAndIsDeletedFalse(Long id);



    @Query("""
SELECT DISTINCT enrollmentYear FROM Classes WHERE isDeleted = false ORDER BY enrollmentYear DESC
""")
    List<Integer> findDistinctEnrollmentYear();
}
