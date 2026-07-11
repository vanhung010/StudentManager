package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.Departments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Departments, Long> {

    //lấy tất car khoa chưa bị xóa
    List<Departments> findAllByIsDeletedFalse();

    //Tìm theo id chưa bị xóa

    Optional<Departments> findByIdAndIsDeletedFalse(Long id);

    //kiểm tra trùng code khi Post
    boolean existsByDepartmentCode(String departmentCode);

    // Kiểm tra trùng code khi PUT (trừ chính nó ra)
    boolean existsByDepartmentCodeAndIdNot(String departmentCode, Long id);

}
