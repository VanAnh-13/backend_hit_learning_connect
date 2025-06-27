package com.example.projectbase.service.impl;

import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.dto.request.user.ChangePassFirstTimeRequest;
import com.example.projectbase.domain.dto.request.user.ChangePassRequest;
import com.example.projectbase.domain.dto.request.user.UserCreateDto;
import com.example.projectbase.domain.dto.request.user.UserUpdateDto;
import com.example.projectbase.domain.dto.response.user.UserResponseDto;
import com.example.projectbase.domain.model.Role;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.mapper.UserMapper;
import com.example.projectbase.exception.extended.InvalidException;
import com.example.projectbase.exception.extended.NotFoundException;
import com.example.projectbase.exception.extended.UnauthorizedException;
import com.example.projectbase.repository.RoleRepository;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;


    //------------------------CRUD User------------------------
    @Override
    @Cacheable(value = "userDto", key = "#id")
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ID, new String[]{id.toString()}));
        return userMapper.toUserResponseDto(user);
    }

    @Override
    public List<UserResponseDto> getUsers(Pageable pageable) {
        return userMapper.toUserResponseDtos(userRepository.findAll(pageable).getContent());
    }

    @Override
    public UserCreateDto createUser(UserCreateDto userCreateDto) throws InvalidException {

        if (userRepository.findByEmail(userCreateDto.getEmail()).isPresent()) {
            throw new InvalidException(ErrorMessage.User.ERR_EMAIL_EXISTED, new String[]{userCreateDto.getEmail()});
        }

        if (userRepository.findByUsername(userCreateDto.getUsername()).isPresent()) {
            throw new InvalidException(ErrorMessage.User.ERR_USER_NAME_EXISTED, new String[]{userCreateDto.getUsername()});
        }


        User user = userMapper.toUser(userCreateDto);

        String roleName = (userCreateDto.getRole() == null || userCreateDto.getRole().isBlank())
                ? "ROLE_USER"
                : userCreateDto.getRole();

        Role role = roleRepository.findByRoleName(roleName);

        if (role == null) {
            throw new NotFoundException(ErrorMessage.Role.ERR_NOT_FOUND_ROLE, new String[]{roleName});
        }

        user.setCreatedDate(LocalDateTime.now());
        user.setLastModifiedDate(user.getCreatedDate());
        user.setLastLogin(user.getCreatedDate());

        user.setRole(role);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userCreateDto;
    }

    @Override
    @CacheEvict(cacheNames = {"userDto", "users"}, key = "#id")
    public UserResponseDto updateUser(Long id, UserUpdateDto updatedUser) {
        return userMapper.toUserResponseDto(
                userRepository.findById(id).map(user -> {

                    if (userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
                        throw new InvalidException(ErrorMessage.User.ERR_EMAIL_EXISTED, new String[]{updatedUser.getEmail()});
                    }

                    if (userRepository.findByUsername(updatedUser.getUsername()).isPresent()) {
                        throw new InvalidException(ErrorMessage.User.ERR_USER_NAME_EXISTED, new String[]{updatedUser.getUsername()});
                    }

                    if (!updatedUser.getEmail().equals("")) {
                        user.setEmail(updatedUser.getEmail());
                    }

                    if (!updatedUser.getFullname().equals("")) {
                        user.setFullName(updatedUser.getFullname());
                    }

                    if (!updatedUser.getUsername().equals("")) {
                        user.setUsername(updatedUser.getUsername());
                    }

                    if (!updatedUser.getAvatarUrl().equals("")) {
                        user.setAvatarUrl(updatedUser.getAvatarUrl());
                    }

                    return userRepository.save(user);
                }).orElseThrow(() -> new RuntimeException(ErrorMessage.User.ERR_USER_NOT_FOUND))
        );
    }


    @Override
    @CacheEvict(cacheNames = {"userDto", "users"}, key = "#id")
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException(ErrorMessage.User.ERR_USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    //------------------------End CRUD------------------------

    //------------------------Common--------------------------
    User getUser(UserPrincipal currentUser) {
        return userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME,
                        new String[]{currentUser.getUsername()}));
    }

    @Override
    public UserResponseDto getCurrentUser(UserPrincipal principal) {
        return getUserById(principal.getId());
    }

    @Override
    public void changePasswordFirstTime(ChangePassFirstTimeRequest changePassFirstTimeRequest, UserPrincipal principal) {
        if (!changePassFirstTimeRequest.getNewPassword().equals(changePassFirstTimeRequest.getConfirmPassword())) {
            throw new UnauthorizedException(ErrorMessage.NOT_CORRECT_PASSWORD);
        }
        User user = getUser(principal);

        if (!user.getLastLogin().equals(user.getCreatedDate())) {
            throw new UnauthorizedException(ErrorMessage.Auth.ERR_CHANGE_PASSWORD_FIST_TIME_LOGIN);
        }

        user.setPassword(passwordEncoder.encode(changePassFirstTimeRequest.getNewPassword()));
        user.setLastLogin(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public String changePassword(ChangePassRequest changePassRequest, UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(
                () -> new RuntimeException(ErrorMessage.User.ERR_USER_NOT_FOUND)
        );

        if (passwordEncoder.matches(changePassRequest.getOldPassword(), user.getPassword())) {
            if (!changePassRequest.getOldPassword().equals(changePassRequest.getNewPassword())) {
                throw new UnauthorizedException(ErrorMessage.NOT_CORRECT_PASSWORD);
            } else {
                userRepository.updatePasswordById(user.getId(), passwordEncoder.encode(changePassRequest.getNewPassword()));
            }
        } else {
            throw new UnauthorizedException(ErrorMessage.Auth.ERR_INCORRECT_PASSWORD);
        }
        return "CHANGE PASSWORD SUCCESS";
    }


}
