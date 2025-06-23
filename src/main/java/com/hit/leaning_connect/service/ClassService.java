package com.hit.leaning_connect.service;

import com.hit.leaning_connect.domain.reponse.ClassResponseDto;
import com.hit.leaning_connect.domain.request.ClassRequestDto;

import java.util.List;

public interface ClassService {
    ClassResponseDto addNewClass(ClassRequestDto classRequestDto);

    ClassResponseDto editClass(Long idClass, ClassRequestDto classRequestDto);

    void deleteClass(Long idClass);

    List<ClassResponseDto> getALlClass();
}
