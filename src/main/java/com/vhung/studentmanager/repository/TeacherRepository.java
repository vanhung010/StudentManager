package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {}  //"JpaSpecificationExecutor" là interface bổ sung để gọi các phương thức tìm kiếm nâng cao, cụ thể ở đây là findAll(Specification, Pageable)