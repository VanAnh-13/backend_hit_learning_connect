package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.request.classes.ClassRequestDto;
import com.example.projectbase.domain.dto.response.classes.ClassResponseDto;
import com.example.projectbase.domain.entity.ClassRoom;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ClassMapper {
    @Autowired
    protected UserRepository userRepository;

    @Mapping(source = "teacherId", target = "teacher", qualifiedByName = "idToUser")
    public abstract ClassRoom toEntity(ClassRequestDto dto);

    @Mapping(source = "teacher.id", target = "teacherId")
    @Mapping(source = "teacher.fullName", target = "teacherFullName")
    @Mapping(source = "teacher.username", target = "teacherUsername")
    @Mapping(target = "status", ignore = true)
    public abstract ClassResponseDto toDTO(ClassRoom entity);

    @AfterMapping
    protected void afterMappingStatus(ClassRoom entity,
                                      @MappingTarget ClassResponseDto dto) {
        dto.setStatus(computeStatus(entity.getStartDate(), entity.getEndDate()));
    }

    public List<ClassRoom> toEntityList(List<ClassRequestDto> dtos) {
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public List<ClassResponseDto> toDTOList(List<ClassRoom> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }


    @Named("idToUser")
    public User idToUser(Long id) {
        if (id == null) return null;
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User không tồn tại với id=" + id));
    }

    protected static String computeStatus(LocalDate start, LocalDate end) {
        LocalDate today = LocalDate.now();
        if (today.isBefore(start))  return "UPCOMING";
        if (today.isAfter(end))     return "COMPLETED";
        return "IN_PROGRESS";
    }
}
