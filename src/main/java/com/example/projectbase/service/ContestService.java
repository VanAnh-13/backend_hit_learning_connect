package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.contest.ContestCreatetDto;
import com.example.projectbase.domain.dto.request.contest.ContestSubmissionRequest;
import com.example.projectbase.domain.dto.request.contest.ContestUpdateDto;
import com.example.projectbase.domain.dto.response.contest.ContestReponseDto;
import com.example.projectbase.domain.dto.response.contest.ContestResultResponse;
import com.example.projectbase.domain.dto.response.contest.ContestUserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ContestService {
  Page<ContestReponseDto> getAllPaged(int page, int size);
  Page<ContestReponseDto> search(String keyword, int  page, int size );
  ContestReponseDto getById(Long id);
  ContestReponseDto createContest(ContestCreatetDto request);;
  ContestReponseDto updateContest(Long id, ContestUpdateDto request);
  void deleteContest(Long id);
  ContestResultResponse  getResultByContestId(Long contestId);
  void joinContest(Long contestId);
  ContestReponseDto startContest(Long contestId);
  void submitContest(ContestSubmissionRequest request, MultipartFile file) throws IOException;
  void autoCloseExpiredContests();

}
