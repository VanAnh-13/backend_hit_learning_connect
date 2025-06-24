package com.example.projectbase.repository;

import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.exception.NotFoundException;
import com.example.projectbase.security.UserPrincipal;
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
public interface UserRepository extends JpaRepository<User, String> {


  @Query("SELECT u FROM User u WHERE u.id = ?1")
  @Cacheable(value = "users", key = "#id")
  Optional<User> findById(String id);


  @Query("SELECT u FROM User u WHERE u.username = ?1")
  @Cacheable(value = "users", key = "#username")
  Optional<User> findByUsername(String username);

  @Cacheable(value = "users", key = "#email")
  @Query("SELECT u FROM User u WHERE u.email = ?1")
  Optional<User> findByEmail(String email);

  @Modifying
  @Transactional
  @CacheEvict(value = "users", key = "#email")
  @Query("UPDATE User u SET u.password = :newPassword WHERE u.email = :email")
  int updatePasswordByEmail(@Param("email") String email,
                            @Param("newPassword") String newPassword);
//
//  default User getUser(UserPrincipal currentUser) {
//    return findByUsername(currentUser.getUsername())
//            .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME,
//                    new String[]{currentUser.getUsername()}));
//  }

  @Query("SELECT u FROM User u")
  Page<User> findAll(Pageable pageable);

}
