package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {
    Optional<Teacher> findByUser_UserName(String username);
    Optional<Teacher> findByIdAndIsDeletedFalse(Long id);
    Optional<Teacher> findByEmail(String email);
    Optional<Teacher> findByUserId(Long userId);

    boolean existsByTeacherCode(String teacherCode);
    boolean existsByTeacherCodeAndIdNot(String teacherCode, Long id);

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneNumberContainingIgnoreCase(String phoneNumber);
    boolean existsByPhoneNumberContainingIgnoreCaseAndIdNot(String phoneNumber, Long id);

    int countByDepartment_IdAndIsDeletedIsFalse(Long departmentId);
}