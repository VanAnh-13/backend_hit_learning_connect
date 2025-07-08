package com.hit.leaning_connect.service;

import com.hit.leaning_connect.domain.reponse.ClassEvaluationResponseDto;
import com.hit.leaning_connect.domain.request.ClassEvaluationRequestDto;

import java.util.List;

public interface ClassEvaluationService {
    List<ClassEvaluationResponseDto> getEvaluationsByClassId(Long classId);

    List<ClassEvaluationResponseDto> getEvaluationsByUserId(Long userId);

    ClassEvaluationResponseDto getEvaluationById(Long id);

    ClassEvaluationResponseDto addEvaluation(ClassEvaluationRequestDto evaluationRequestDto, Long userId);

    ClassEvaluationResponseDto updateEvaluation(Long id, ClassEvaluationRequestDto evaluationRequestDto);

    void deleteEvaluation(Long id);
}
