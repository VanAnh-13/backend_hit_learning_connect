package com.example.projectbase.service;

import com.example.projectbase.domain.dto.pagination.PaginationFullRequestDto;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDto;
import com.example.projectbase.domain.dto.request.ChangePassFirstTimeRequest;
import com.example.projectbase.domain.dto.request.UserCreateDto;
import com.example.projectbase.domain.dto.request.UserUpdateDto;
import com.example.projectbase.domain.dto.response.UserDto;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

  UserDto getUserById(String userId);

  List<UserDto> getUsers(Pageable pageable);

  UserDto getCurrentUser(UserPrincipal principal);

  void changePassword(@Valid ChangePassFirstTimeRequest changePassFirstTimeRequest, UserPrincipal principal);

  User createUser(User user);
  List<User> getAllUsers();
  Optional<User> getUsersById(String id);
  Optional<User> getUserByUsername(String username);
  User updateUser(String id, UserUpdateDto userUpdateDto);
  void deleteUser(String id);

}
