package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.classes.ClassRequestDto;
import com.example.projectbase.domain.dto.response.classes.ClassResponseDto;
import com.example.projectbase.domain.dto.response.classes.TotalResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ClassService {
    ClassResponseDto getClassById(Long id);

    ClassResponseDto addNewClass(ClassRequestDto classRequestDto);

    ClassResponseDto editClass(Long idClass, ClassRequestDto classRequestDto);

    void deleteClass(Long idClass);

    List<ClassResponseDto> getALlClass();

    List<ClassResponseDto> getALlClassByFilter(Pageable pageable, String keyword);

}
