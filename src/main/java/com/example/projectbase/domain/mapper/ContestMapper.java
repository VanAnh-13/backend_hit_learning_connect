package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.request.contest.ContestCreatetDto;
import com.example.projectbase.domain.dto.request.contest.ContestUpdateDto;
import com.example.projectbase.domain.dto.response.contest.ContestResponseDto;
//import com.example.projectbase.domain.dto.response.contest.ContestResponseDto;
import com.example.projectbase.domain.dto.response.contest.ContestResultResponse;
import com.example.projectbase.domain.dto.response.contest.ContestUserResponseDto;
import com.example.projectbase.domain.entity.Contest;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")

public interface ContestMapper {

   ContestMapper INSTANCE = Mappers.getMapper(ContestMapper.class);
   @Mapping(source = "contestId", target = "contestId")
   @Mapping(source = "title", target = "title")
   @Mapping(source = "resultSummary", target = "resultSummary")
   @Mapping(source = "ranking", target = "ranking")
   @Mapping(source = "highestScore", target = "highestScore")
   ContestResponseDto toReponse(Contest contest);

   @AfterMapping
   default void setStatus(@MappingTarget ContestResponseDto response, Contest contest) {
      LocalDateTime now = LocalDateTime.now();
      if (now.isBefore(contest.getStartTime())) {
         response.setStatus("Sắp diễn ra");
      } else if (now.isAfter(contest.getEndTime())) {
         response.setStatus("Đã kết thúc");
      } else {
         response.setStatus("Đang mở");
      }
   }

   default Page<ContestResponseDto> toResponseList(Page<Contest> contests) {
      return contests.map(contest -> toReponse(contest));
   };

   @Mapping(source = "username", target = "username")
   ContestResultResponse toResultResponse(Contest contest, String username);

   Contest toEntity(ContestCreatetDto request);

   void updateEntity(@MappingTarget Contest contest, ContestUpdateDto request);

//   ContestUserResponseDto toUserResponse(Contest contest);
}
