package com.example.projectbase.service.impl;

import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.dto.request.user.ChangePassFirstTimeRequest;
import com.example.projectbase.domain.dto.request.user.ChangePassRequest;
import com.example.projectbase.domain.dto.request.user.UserCreateDto;
import com.example.projectbase.domain.dto.request.user.UserUpdateDto;
import com.example.projectbase.domain.dto.response.user.ListUserResponseDto;
import com.example.projectbase.domain.dto.response.user.UserResponseDto;
import com.example.projectbase.domain.entity.ClassRoom;
import com.example.projectbase.domain.entity.Contest;
import com.example.projectbase.domain.model.Role;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.mapper.UserMapper;
import com.example.projectbase.exception.extended.InvalidException;
import com.example.projectbase.exception.extended.NotFoundException;
import com.example.projectbase.exception.extended.UnauthorizedException;
import com.example.projectbase.repository.ClassRepository;
import com.example.projectbase.repository.ContestRepository;
import com.example.projectbase.repository.RoleRepository;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.UserService;
import com.nimbusds.openid.connect.sdk.claims.Gender;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final ClassRepository classRepository;

    private final ContestRepository contestRepository;

    //------------------------CRUD User------------------------
    @Override
    @Cacheable(value = "userDto", key = "#id")
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ID, new String[]{id.toString()}));
        return userMapper.toUserResponseDto(user);
    }

    @Override
    public ListUserResponseDto getUsers(Pageable pageable) {
        return ListUserResponseDto.builder()
                .data(userMapper.toUserResponseDtos(userRepository.findAll(pageable).getContent()))
                .amountOfAllUsers(userRepository.count())
                .build();
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

                    System.out.println(user.getPassword());

                    if (userRepository.findByEmail(updatedUser.getEmail()).isPresent() && !user.getEmail().equals(updatedUser.getEmail())) {
                        throw new InvalidException(ErrorMessage.User.ERR_EMAIL_EXISTED, new String[]{updatedUser.getEmail()});
                    }

                    if (userRepository.findByUsername(updatedUser.getUsername()).isPresent() && !user.getUsername().equals(updatedUser.getUsername())) {
                        throw new InvalidException(ErrorMessage.User.ERR_USER_NAME_EXISTED, new String[]{updatedUser.getUsername()});
                    }

                    if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                        user.setEmail(updatedUser.getEmail());
                    }

                    if (updatedUser.getFullname() != null && !updatedUser.getFullname().isEmpty()) {
                        user.setFullName(updatedUser.getFullname());
                    }

                    if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
                        user.setUsername(updatedUser.getUsername());
                    }

                    if (updatedUser.getAvatarUrl() != null && !updatedUser.getAvatarUrl().isEmpty()) {
                        user.setAvatarUrl(updatedUser.getAvatarUrl());
                    }

                    if (updatedUser.getBirthday() != null && !updatedUser.getBirthday().isEmpty()) {
                        user.setBirthday(LocalDate.parse(updatedUser.getBirthday()));
                    }

                    if (updatedUser.getGender() != null) {
                        if (updatedUser.getGender().equals("MALE")) {
                            user.setGender("MALE");
                        }

                        if (updatedUser.getGender().equals("FEMALE")) {
                            user.setGender("FEMALE");
                        }
                    }
                    return userRepository.save(user);
                }).orElseThrow(() -> new RuntimeException(ErrorMessage.User.ERR_USER_NOT_FOUND))
        );
    }


    @Override
    @CacheEvict(cacheNames = {"userDto", "users"}, key = "#id")
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(ErrorMessage.User.ERR_USER_NOT_FOUND));

        Set<Contest> contests = user.getContests();
        for (Contest contest : contests) {
            contest.getParticipants().remove(user);
        }
        contestRepository.saveAll(contests);

        ClassRoom classRoom = user.getClassRoom();
        if (classRoom != null) {
            classRoom.getUsers().remove(user);
            classRepository.save(classRoom);
            user.setClassRoom(null);
        }

        List<ClassRoom> taughtClasses = classRepository.findByTeacher(user);
        for (ClassRoom cls : taughtClasses) {
            cls.setTeacher(null);
        }
        classRepository.saveAll(taughtClasses);

        userRepository.delete(user);

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
    @CacheEvict(cacheNames = {"userDto", "users"}, key = "#userPrincipal.id")
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

    @Override
    public Boolean checkFirstLogin(UserPrincipal userPrincipal) {
        User user = getUser(userPrincipal);

        return (user.getLastLogin().equals(user.getCreatedDate()));
    }

    @Override
    public ListUserResponseDto getUsersByFilter(Pageable pageable, String keyword) {
        return ListUserResponseDto.builder()
                .data(userMapper.toUserResponseDtos(userRepository.searchByKeyword(keyword, pageable).getContent()))
                .amountOfAllUsers(userRepository.count())
                .build();
    }

}
