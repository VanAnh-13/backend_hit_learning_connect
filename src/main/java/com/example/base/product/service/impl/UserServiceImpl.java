package com.example.base.product.service.impl;

import com.example.base.product.domain.entity.User;
import com.example.base.product.domain.reponse.UserReponse;
import com.example.base.product.domain.request.UserCreateRequest;
import com.example.base.product.domain.request.UserUpdateRequest;
import com.example.base.product.repository.UserRepository;
import com.example.base.product.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    ModelMapper modelMapper;

    @Override
    public UserReponse createUser(UserCreateRequest request) {
        User users = modelMapper.map(request, User.class);
        userRepository.save(users);
        return modelMapper.map(users, UserReponse.class);
    }

    @Override
    public UserReponse updateUser(UserUpdateRequest request) {
        return null;
    }

    @Override
    public boolean deleteUser(Long id) {
        return false;
    }

    @Override
    public UserReponse getUserId(Long id) {
        return null;
    }

    @Override
    public List<UserReponse> getUsers() {
        return List.of();
    }
}
