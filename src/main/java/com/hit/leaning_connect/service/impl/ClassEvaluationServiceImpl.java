package com.hit.leaning_connect.service.impl;

import com.hit.leaning_connect.domain.entity.Class;
import com.hit.leaning_connect.domain.entity.ClassEvaluation;
import com.hit.leaning_connect.domain.entity.User;
import com.hit.leaning_connect.domain.mapper.ClassEvaluationMapper;
import com.hit.leaning_connect.domain.reponse.ClassEvaluationResponseDto;
import com.hit.leaning_connect.domain.request.ClassEvaluationRequestDto;
import com.hit.leaning_connect.exception.ResourceNotFoundException;
import com.hit.leaning_connect.repository.ClassEvaluationRepository;
import com.hit.leaning_connect.repository.CourseRepository;
import com.hit.leaning_connect.repository.UserRepository;
import com.hit.leaning_connect.service.ClassEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassEvaluationServiceImpl implements ClassEvaluationService {

    private final ClassEvaluationRepository evaluationRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ClassEvaluationMapper evaluationMapper;

    @Override
    public List<ClassEvaluationResponseDto> getEvaluationsByClassId(Long classId) {
        return evaluationRepository.findByClassObjClassId(classId).stream()
                .map(evaluationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassEvaluationResponseDto> getEvaluationsByUserId(Long userId) {
        return evaluationRepository.findByUserUserId(userId).stream()
                .map(evaluationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClassEvaluationResponseDto getEvaluationById(Long id) {
        return evaluationRepository.findById(id)
                .map(evaluationMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation", "id", id));
    }

    @Override
    public ClassEvaluationResponseDto addEvaluation(ClassEvaluationRequestDto evaluationRequestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Class classObj = courseRepository.findById(evaluationRequestDto.classId())
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", evaluationRequestDto.classId()));
        
        ClassEvaluation evaluation = evaluationMapper.toEntity(evaluationRequestDto, classObj, user);
        ClassEvaluation savedEvaluation = evaluationRepository.save(evaluation);
        
        return evaluationMapper.toDTO(savedEvaluation);
    }

    @Override
    public ClassEvaluationResponseDto updateEvaluation(Long id, ClassEvaluationRequestDto evaluationRequestDto) {
        ClassEvaluation existingEvaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation", "id", id));
        
        // Update only rating and comment, not the class or user
        existingEvaluation.setRating(evaluationRequestDto.rating());
        existingEvaluation.setComment(evaluationRequestDto.comment());
        
        ClassEvaluation updatedEvaluation = evaluationRepository.save(existingEvaluation);
        return evaluationMapper.toDTO(updatedEvaluation);
    }

    @Override
    public void deleteEvaluation(Long id) {
        if (!evaluationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Evaluation", "id", id);
        }
        evaluationRepository.deleteById(id);
    }
}
