package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.contest.ContestCreatetDto;
import com.example.projectbase.domain.dto.response.contest.ContestReponseDto;
import org.springframework.data.domain.Page;

public interface ContestService {
  Page<ContestReponseDto> getAllPaged(int page, int size);
  Page<ContestReponseDto> search(String keyword, int  page, int size );
  ContestReponseDto getById(Long id);
  ContestReponseDto createContest(ContestCreatetDto request);;
  ContestReponseDto updateContest(Long id, ContestCreatetDto request);
  void deleteContest(Long id);

}
