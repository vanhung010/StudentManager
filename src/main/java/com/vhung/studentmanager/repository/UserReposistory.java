package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.User;
import org.hibernate.boot.models.JpaAnnotations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserReposistory extends JpaRepository<User, Long> {
}