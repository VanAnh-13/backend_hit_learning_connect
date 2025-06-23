package com.hit.leaning_connect.repository;

import com.hit.leaning_connect.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
