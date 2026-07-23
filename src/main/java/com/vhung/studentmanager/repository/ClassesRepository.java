package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.Classes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassesRepository extends JpaRepository<Classes, Long> {

    Optional<Classes> findByIdAndIsDeletedFalse(Long id);
}
