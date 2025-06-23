package com.example.projectbase.service.impl;

import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.constant.SortByDataConstant;
import com.example.projectbase.domain.dto.pagination.PaginationFullRequestDto;
import com.example.projectbase.domain.dto.pagination.PaginationResponseDto;
import com.example.projectbase.domain.dto.request.ChangePassFirstTimeRequest;
import com.example.projectbase.domain.dto.response.UserDto;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.mapper.UserMapper;
import com.example.projectbase.exception.NotFoundException;
import com.example.projectbase.exception.UnauthorizedException;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.UserService;
import com.example.projectbase.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  private final PasswordEncoder passwordEncoder;

  @Override
  @Cacheable(value = "userDto", key = "#userId")
  public UserDto getUserById(String userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ID, new String[]{userId}));
    return userMapper.toUserDto(user);
  }

  @Override
  public List<UserDto> getUsers(Pageable pageable) {
      return userMapper.toUserDtos(userRepository.findAll(pageable).getContent());
//    return new PaginationResponseDto<>(null, null);
  }

  @Override
  public UserDto getCurrentUser(UserPrincipal principal) {
    User user = userRepository.getUser(principal);
    return userMapper.toUserDto(user);
  }

  @Override
  public void changePassword(ChangePassFirstTimeRequest changePassFirstTimeRequest, UserPrincipal principal) {
    if (!changePassFirstTimeRequest.getNewPassword().equals(changePassFirstTimeRequest.getConfirmPassword())) {
      throw new UnauthorizedException(ErrorMessage.NOT_CORRECT_PASSWORD);
    }
      User user = userRepository.getUser(principal);

    if (!user.getLastLogin().equals(user.getCreatedDate())) {
      throw new UnauthorizedException(ErrorMessage.Auth.ERR_CHANGE_PASSWORD_FIST_TIME_LOGIN);
    }

    user.setPassword(passwordEncoder.encode(changePassFirstTimeRequest.getNewPassword()));
    user.setLastLogin(LocalDateTime.now());

    userRepository.save(user);
  }

  @Override
  public User createUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public Optional<User> getUsersById(String id) {
    return userRepository.findById(id);
  }

  @Override
  public Optional<User> getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public User updateUser(String id, User updatedUser) {
    return userRepository.findById(id).map(user -> {
//      user.setFullname(updatedUser.getFullname());
      user.setEmail(updatedUser.getEmail());
      user.setUsername(updatedUser.getUsername());
      user.setPassword(updatedUser.getPassword());
      return userRepository.save(user);
    }).orElseThrow(() -> new RuntimeException(ErrorMessage.User.ERR_USER_NOT_FOUND));
  }

  @Override
  public void deleteUser(String id) {
    if (!userRepository.existsById(id)) {
      throw new RuntimeException(ErrorMessage.User.ERR_USER_NOT_FOUND);
    }
    userRepository.deleteById(id);
  }

}
