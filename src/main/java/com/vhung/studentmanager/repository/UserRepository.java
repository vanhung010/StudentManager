package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.enums.Role;
import com.vhung.studentmanager.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByUserName(String username);
    //kiểm tra trùng userName khi cập nhật
    boolean existsUserByUserNameAndIdNot(String userName, Long id);


    Optional<User> findByIdAndIsDeletedFalse(Long aLong);

    Page<User> findAllByIsDeletedFalse(Pageable pageable);

    Page<User> findAllByIsDeletedFalseAndRole(Role role, Pageable pageable);

}