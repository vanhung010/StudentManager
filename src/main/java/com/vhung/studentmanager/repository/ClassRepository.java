package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<Classes, Long>, JpaSpecificationExecutor<Classes> {

    Optional<Classes> findByIdAndIsDeletedFalse(Long id);


}
