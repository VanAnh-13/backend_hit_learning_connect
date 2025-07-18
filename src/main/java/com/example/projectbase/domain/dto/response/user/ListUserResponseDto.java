package com.example.projectbase.domain.dto.response.user;

import lombok.Builder;

import java.util.List;

@Builder
public class ListUserResponseDto {
    List<UserResponseDto> data;
    Long amountOfAllUsers;
}
