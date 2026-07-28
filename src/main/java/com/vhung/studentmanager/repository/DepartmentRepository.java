package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.Departments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Departments, Long> {

    //lấy tất car khoa chưa bị xóa
    Page<Departments> findAllByIsDeletedFalse(Pageable pageable);
    Page<Departments> findAllByIsDeletedTrue(Pageable pageable);

    //Tìm theo id chưa bị xóa

    Optional<Departments> findByIdAndIsDeletedFalse(Long id);

    Optional<Departments> findByIdAndIsDeletedTrue(Long id);

    //kiểm tra trùng code khi Post
    boolean existsByDepartmentCode(String departmentCode);

    // Kiểm tra trùng code khi PUT (trừ chính nó ra)
    boolean existsByDepartmentCodeAndIdNot(String departmentCode, Long id);

    @Query("""
     SELECT d FROM Departments d
            WHERE d.isDeleted = false
            AND (LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                 OR LOWER(d.departmentCode) LIKE LOWER(CONCAT('%', :keyword, '%')))
""")
    Page<Departments> search(@Param("keyword") String keyword, Pageable pageable);
}
