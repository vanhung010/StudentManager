package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    int countByClassesIdAndIsDeletedIsFalse(Long classid);

    boolean existsStudentByStudentCode(String studentCode);

    boolean existsStudentByStudentCodeAndIdNot(String studentCode, Long id);

    boolean existsStudentByEmail(String email);

    boolean existsStudentByEmailAndIdNot(String email, Long id);

    Optional<Student> findById(Long id);

    Optional<Student> findByIdAndIsDeletedFalse(Long id);

    Page<Student> findAllByIsDeletedFalse(Pageable pageable);
    
    Page<Student> findAllByIsDeletedFalseAndFullNameContainingIgnoreCase(
            String name, Pageable pageable);

    Page<Student> findAllByIsDeletedFalseAndDepartmentsId(
            Long departmentId, Pageable pageable);

    Page<Student> findAllByIsDeletedFalseAndFullNameContainingIgnoreCaseAndDepartmentsId(
            String name, Long departmentId, Pageable pageable);

    Optional<Student> findByEmail(String email);

    Optional<Student> findByUserId(Long userId);

    List<Student> findAllByClasses_Id(Long classesId);

    boolean existsByStudentCode(String studentCode);
    boolean existsByEmail(String email);
}
