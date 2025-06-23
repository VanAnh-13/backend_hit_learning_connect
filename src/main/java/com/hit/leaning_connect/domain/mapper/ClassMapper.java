package com.hit.leaning_connect.domain.mapper;

import com.hit.leaning_connect.domain.reponse.ClassResponseDto;
import com.hit.leaning_connect.domain.request.ClassRequestDto;
import org.mapstruct.Mapper;
import java.util.List;
import com.hit.leaning_connect.domain.entity.Class;

@Mapper(componentModel = "spring")
public interface ClassMapper {
    ClassResponseDto toDTO(Class entity);

    Class toEntity(ClassRequestDto dto);

    List<ClassResponseDto> toDTOList(List<Class> entities);

    List<Class> toEntityList(List<ClassRequestDto> classRequestDtos);
}
