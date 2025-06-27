package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.request.classes.ClassRequestDto;
import com.example.projectbase.domain.dto.response.classes.ClassResponseDto;
import com.example.projectbase.domain.entity.ClassRoom;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassMapper {
    ClassResponseDto toDTO(ClassRoom entity);

    ClassRoom toEntity(ClassRequestDto dto);

    List<ClassResponseDto> toDTOList(List<ClassRoom> entities);

    List<ClassRoom> toEntityList(List<ClassRequestDto> classRequestDtos);
}