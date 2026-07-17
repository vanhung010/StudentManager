package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.User;
import org.hibernate.boot.models.JpaAnnotations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserReposistory extends JpaRepository<User, Long> {

    boolean existsUserByUserName(String username);
    //kiểm tra trùng userName khi cập nhật
    boolean existsUserByUserNameAndIdNot(String userName, Long id);

}