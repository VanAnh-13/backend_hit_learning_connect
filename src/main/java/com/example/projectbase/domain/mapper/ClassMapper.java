package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.request.classes.ClassRequestDto;
import com.example.projectbase.domain.dto.response.classes.ClassResponseDto;
import com.example.projectbase.domain.entity.Class;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassMapper {
    ClassResponseDto toDTO(Class entity);

    Class toEntity(ClassRequestDto dto);

    List<ClassResponseDto> toDTOList(List<Class> entities);

    List<Class> toEntityList(List<ClassRequestDto> classRequestDtos);
}