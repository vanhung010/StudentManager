package com.vhung.studentmanager.repository;

import com.vhung.studentmanager.entity.Admins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminReposistory extends JpaRepository<Admins, Long> {
    Optional<Admins> findByEmail(String email);

    Optional<Admins> findByUserId(Long userId);
}
