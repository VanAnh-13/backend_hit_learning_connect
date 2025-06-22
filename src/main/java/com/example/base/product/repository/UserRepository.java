package com.example.base.product.repository;

import com.example.base.product.constant.ErrorMessage;
import com.example.base.product.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByName(String username);
    Optional<User> findByUsername(String username);
}
