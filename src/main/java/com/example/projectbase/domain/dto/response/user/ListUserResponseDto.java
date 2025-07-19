package com.example.projectbase.domain.dto.response.user;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ListUserResponseDto {
    List<UserResponseDto> data;
    Long amountOfAllUsers;
}
