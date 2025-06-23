package com.hit.leaning_connect.service.impl;

import com.hit.leaning_connect.domain.entity.Class;
import com.hit.leaning_connect.domain.mapper.ClassMapper;
import com.hit.leaning_connect.domain.reponse.ClassResponseDto;
import com.hit.leaning_connect.domain.request.ClassRequestDto;
import com.hit.leaning_connect.exception.ResourceNotFoundException;
import com.hit.leaning_connect.repository.ClassRepository;
import com.hit.leaning_connect.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {
    private final ClassRepository classRepository;
    private final ClassMapper classMapper;

    @Override
    public ClassResponseDto addNewClass(ClassRequestDto classRequestDto) {
        Class classAdd = classMapper.toEntity(classRequestDto);
        Class addSuccess = classRepository.save(classAdd);

        return classMapper.toDTO(addSuccess);
    }

    @Override
    public ClassResponseDto editClass(Long idClass, ClassRequestDto classRequestDto) {
        Class classExist = classRepository.findById(idClass)
            .orElseThrow(() -> new ResourceNotFoundException("Class", "id", idClass));

        classExist.setDescription(classRequestDto.description());
        classExist.setTitle(classRequestDto.title());
        classExist.setTeacher(classRequestDto.t);
        return null;
    }

    @Override
    public void deleteClass(Long idClass) {

    }

    @Override
    public List<ClassResponseDto> getALlClass() {
        return List.of();
    }
}
