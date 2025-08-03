package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.contest.ContestCreatetDto;
import com.example.projectbase.domain.dto.request.contest.ContestSubmissionRequest;
import com.example.projectbase.domain.dto.request.contest.ContestUpdateDto;
//import com.example.projectbase.domain.dto.response.contest.ContestAdminResponseDto;
import com.example.projectbase.domain.dto.response.contest.ContestResponseDto;
import com.example.projectbase.domain.dto.response.contest.ContestResultResponse;
import com.example.projectbase.domain.dto.response.contest.ContestUserResponseDto;
import com.example.projectbase.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ContestService {
//  Page<ContestAdminResponseDto> getAllPagedForAdmin(int page, int size);
  Page<ContestResponseDto> getAllPaged(int page, int size);
  Page<ContestResponseDto> search(String keyword, int  page, int size );
  ContestResponseDto getById(Long id);
  ContestResponseDto createContest(ContestCreatetDto request);
  ContestResponseDto updateContest(Long id, ContestUpdateDto request);
  void deleteContest(Long id);
  ContestResultResponse  getResultByContestId(Long contestId);
  void joinContest(Long contestId, UserPrincipal userPrincipal);
  ContestResponseDto startContest(Long contestId);
  void submitContest(ContestSubmissionRequest request, MultipartFile file) throws IOException;
  void autoCloseExpiredContests();
}
