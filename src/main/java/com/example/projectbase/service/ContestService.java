package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.contest.ContestCreatetDto;
import com.example.projectbase.domain.dto.request.contest.ContestUpdateDto;
import com.example.projectbase.domain.dto.response.contest.ContestReponseDto;
import com.example.projectbase.domain.dto.response.contest.ContestResultResponse;
import com.example.projectbase.domain.dto.response.contest.ContestUserResponseDto;
import org.springframework.data.domain.Page;

public interface ContestService {
  Page<ContestReponseDto> getAllPaged(int page, int size);
  Page<ContestUserResponseDto> getAllPagedUser(String username, int page, int size);
  Page<ContestReponseDto> search(String keyword, int  page, int size );
  ContestReponseDto getById(Long id);
  ContestReponseDto createContest(ContestCreatetDto request);;
  ContestReponseDto updateContest(Long id, ContestUpdateDto request);
  void deleteContest(Long id);
  ContestResultResponse  getResultByContestId(Long contestId);

}
