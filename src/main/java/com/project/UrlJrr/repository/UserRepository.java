package com.project.UrlJrr.repository;

import com.project.UrlJrr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndUsername(String email, String username);
    
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findBySubScribe(boolean b);
}
