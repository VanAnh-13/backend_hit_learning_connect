package com.hit.leaning_connect.domain.mapper;

import com.hit.leaning_connect.domain.entity.ClassEvaluation;
import com.hit.leaning_connect.domain.entity.Class;
import com.hit.leaning_connect.domain.entity.User;
import com.hit.leaning_connect.domain.reponse.ClassEvaluationResponseDto;
import com.hit.leaning_connect.domain.request.ClassEvaluationRequestDto;
import org.springframework.stereotype.Component;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassEvaluationMapper {
    @Mapping(target = "classObj", source = "classObj")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "rating", source = "dto.rating")
    @Mapping(target = "comment", source = "dto.comment")
    ClassEvaluation toEntity(ClassEvaluationRequestDto dto, Class classObj, User user);

    @Mapping(source = "classObj.classId", target = "classId")
    @Mapping(source = "classObj.title", target = "classTitle")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.fullName", target = "userFullName")
    ClassEvaluationResponseDto toDTO(ClassEvaluation evaluation);
}
