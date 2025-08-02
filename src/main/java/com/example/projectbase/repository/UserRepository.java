package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Document;
import com.example.projectbase.domain.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


  @Query("SELECT u FROM User u WHERE u.id = ?1")
  @Cacheable(value = "users", key = "#id")
  Optional<User> findById(Long id);

  @Query("SELECT u FROM User u WHERE u.username = ?1")
  Optional<User> findByUsernameIgnoreCase(String username);

  @Query("SELECT u FROM User u WHERE u.email = ?1")
  Optional<User> findByEmail(String email);

  @Query("SELECT u FROM User u")
  Page<User> findAll(Pageable pageable);

  @Modifying
  @Transactional
  @Query("UPDATE User u SET u.password = :newPassword WHERE u.email = :email")
  int updatePasswordByEmail(@Param("email") String email,
                            @Param("newPassword") String newPassword);


  @Modifying
  @Transactional
  @Query("UPDATE User u SET u.password = :newPassword WHERE u.id = :id")
  int updatePasswordById(@Param("id") Long id, @Param("newPassword") String newPassword);


  @Query("""
        SELECT u
        FROM User u
        WHERE LOWER(u.username)    LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))   
    """)
  Page<User> searchByKeyword(
          String keyword,
          Pageable pageable
  );



}
