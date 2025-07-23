package com.example.projectbase.service.impl;

import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.dto.request.classes.ClassRequestDto;
import com.example.projectbase.domain.dto.response.classes.ClassResponseDto;
import com.example.projectbase.domain.entity.ClassRoom;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.mapper.ClassMapper;
import com.example.projectbase.exception.extended.NotFoundException;
import com.example.projectbase.repository.ClassRepository;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClassServiceImpl implements ClassService {
    private final ClassRepository classRepository;
    private final ClassMapper classMapper;
    private final UserRepository userRepository;

    @Override
    public ClassResponseDto getClassById(Long id) {
        return classRepository.findById(id)
                .map(classMapper::toDTO)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ClassRoom.CLASS_NOT_FOUND, new String[]{id.toString()}));
    }

    @Override
    public ClassResponseDto addNewClass(ClassRequestDto classRequestDto) {
        ClassRoom classAdd = classMapper.toEntity(classRequestDto);
        ClassRoom addSuccess = classRepository.save(classAdd);

        return classMapper.toDTO(addSuccess);
    }

    @Override
    public ClassResponseDto editClass(Long idClass, ClassRequestDto classRequestDto) {
        ClassRoom classExist = classRepository.findById(idClass)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ClassRoom.CLASS_NOT_FOUND, new String[]{idClass.toString()}));

        classExist.setDescription(classRequestDto.description());
        classExist.setTitle(classRequestDto.title());

        User teacher = userRepository.findById(classRequestDto.teacherId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ClassRoom.CLASS_NOT_FOUND, new String[]{classRequestDto.teacherId().toString()}));

        classExist.setTeacher(teacher);
        classExist.setStartDate(classRequestDto.startDate());
        classExist.setEndDate(classRequestDto.endDate());
        return classMapper.toDTO(classExist);
    }

    @Override
    public void deleteClass(Long idClass) {
        ClassRoom cls = classRepository.findById(idClass).orElseThrow(
                () -> new NotFoundException(ErrorMessage.ClassRoom.CLASS_NOT_FOUND, new String[]{idClass.toString()})
        );
        classRepository.delete(cls);
    }

    @Override
    public List<ClassResponseDto> getALlClass() {
        return classMapper.toDTOList(classRepository.findAll());
    }

    @Override
    public List<ClassResponseDto> getALlClassByFilter(Pageable pageable, String keyword) {
        return classMapper.toDTOList(classRepository.searchByKeyword(keyword, pageable).getContent());
    }
}