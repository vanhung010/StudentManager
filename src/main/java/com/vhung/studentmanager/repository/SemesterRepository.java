package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.Semesters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SemesterRepository extends JpaRepository<Semesters, Long>, JpaSpecificationExecutor<Semesters> {
    Optional<Semesters> findByIsActiveIsTrue();

    Boolean existsBySemesterCode(String semesterCode);
}
