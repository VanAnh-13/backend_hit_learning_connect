package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.request.contest.ContestCreatetDto;
import com.example.projectbase.domain.dto.response.contest.ContestReponseDto;
import com.example.projectbase.domain.entity.Contest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface ContestMapper {
   ContestReponseDto toReponse(Contest contest);

   Contest toEntity(ContestCreatetDto request);

   void updateEntity(@MappingTarget Contest contest, ContestCreatetDto request);

}
