package com.example.projectbase.domain.mapper;


import com.example.projectbase.domain.dto.request.user.UserCreateDto;
import com.example.projectbase.domain.dto.response.user.UserResponseDto;
import com.example.projectbase.domain.entity.User;
import com.nimbusds.openid.connect.sdk.claims.Gender;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", ignore = true)
    User toUser(UserCreateDto userCreateDTO);

    @Mapping(source = "role.name", target = "roleName")
    UserResponseDto toUserResponseDto(User user);

    List<UserResponseDto> toUserResponseDtos(List<User> user);


}
