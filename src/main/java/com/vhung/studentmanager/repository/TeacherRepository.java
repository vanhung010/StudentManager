package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {
    Optional<Teacher> findByUser_Username(String username);
}

//"JpaSpecificationExecutor" là interface bổ sung để gọi các phương thức tìm kiếm nâng cao, cụ thể ở đây là findAll(Specification, Pageable)