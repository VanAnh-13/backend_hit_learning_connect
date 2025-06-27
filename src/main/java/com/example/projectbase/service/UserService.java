package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.ChangePassFirstTimeRequest;
import com.example.projectbase.domain.dto.request.ChangePassRequest;
import com.example.projectbase.domain.dto.request.UserCreateDto;
import com.example.projectbase.domain.dto.request.UserUpdateDto;
import com.example.projectbase.domain.dto.response.UserResponseDto;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.exception.extended.InvalidException;
import com.example.projectbase.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

  UserResponseDto getUserById(Long userId);

  List<UserResponseDto> getUsers(Pageable pageable);

  UserResponseDto getCurrentUser(UserPrincipal principal);

  void changePasswordFirstTime(@Valid ChangePassFirstTimeRequest changePassFirstTimeRequest, UserPrincipal principal);

  UserCreateDto createUser(UserCreateDto user) throws InvalidException;

  UserResponseDto updateUser(Long id, UserUpdateDto updatedUser);

  void deleteUser(Long id);

  String changePassword(ChangePassRequest changePassRequest, UserPrincipal userPrincipal);

}
