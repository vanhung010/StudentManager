package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.Semesters;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SemesterRepository extends JpaRepository<Semesters, Long> {
    Optional<Semesters> findByIsActiveIsTrue();

    Boolean existsBySemesterCode(String semesterCode);
}
