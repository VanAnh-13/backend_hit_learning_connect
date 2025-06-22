package com.example.base.product.service;

import com.example.base.product.domain.entity.User;
import com.example.base.product.domain.reponse.UserReponse;
import com.example.base.product.domain.request.UserCreateRequest;
import com.example.base.product.domain.request.UserUpdateRequest;

import java.util.List;

public interface UserService {

    UserReponse createUser(UserCreateRequest request);
    UserReponse updateUser(UserUpdateRequest request);
    boolean deleteUser(Long id);
    UserReponse getUserId(Long id);
    List<UserReponse> getUsers();
}
