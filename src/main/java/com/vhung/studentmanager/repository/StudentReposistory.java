package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentReposistory extends JpaRepository<Student, Long> {
    boolean existsStudentByStudentCode(String studentCode);

    boolean existsStudentByStudentCodeAndIdNot(String studentCode, Long id);

    boolean existsStudentByEmail(String email);

    boolean existsStudentByEmailAndIdNot(String email, Long id);

    Optional<Student> findById(Long id);

    Optional<Student> findByIdAndIsDeletedFalse(Long id);

    Page<Student> findAllByIsDeletedFalse(Pageable pageable);
}
