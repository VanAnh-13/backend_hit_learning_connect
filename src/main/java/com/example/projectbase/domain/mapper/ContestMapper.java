package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.request.contest.ContestCreatetDto;
import com.example.projectbase.domain.dto.request.contest.ContestUpdateDto;
import com.example.projectbase.domain.dto.response.contest.ContestReponseDto;
import com.example.projectbase.domain.dto.response.contest.ContestResultResponse;
import com.example.projectbase.domain.entity.Contest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")

public interface ContestMapper {

   ContestMapper INSTANCE = Mappers.getMapper(ContestMapper.class);
   @Mapping(source = "contestId", target = "contestId")
   @Mapping(source = "title", target = "title")

   ContestResultResponse toResultResponse(Contest contest);

   ContestReponseDto toReponse(Contest contest);

   Contest toEntity(ContestCreatetDto request);

   void updateEntity(@MappingTarget Contest contest, ContestUpdateDto request);
}
