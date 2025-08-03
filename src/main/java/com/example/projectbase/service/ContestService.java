package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.contest.ContestCreatetDto;
import com.example.projectbase.domain.dto.request.contest.ContestSubmissionRequest;
import com.example.projectbase.domain.dto.request.contest.ContestUpdateDto;
import com.example.projectbase.domain.dto.response.contest.ContestResponseDto;
import com.example.projectbase.domain.dto.response.contest.ContestResultResponse;
import com.example.projectbase.domain.dto.response.contest.ContestSubmissionResponse;
import com.example.projectbase.domain.dto.response.contest.ContestUserResponseDto;
import com.example.projectbase.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ContestService {
  Page<ContestResponseDto> getAll(Pageable pageable);
  Page<ContestResponseDto> search(String keyword, Pageable pageable);
  ContestResponseDto getById(Long id);
  ContestResponseDto createContest(ContestCreatetDto request, MultipartFile file);
  ContestResponseDto updateContest(Long id, ContestUpdateDto request);
  void deleteContest(Long id);
  ContestResultResponse  getResultByContestId(Long contestId);
  void joinContest(Long contestId, UserPrincipal userPrincipal);
  ContestResponseDto startContest(Long contestId);
  ContestSubmissionResponse submitContest(String urlFile, Long contestId, UserPrincipal userPrincipal) throws IOException;
  void autoCloseExpiredContests();
  Page<ContestSubmissionResponse> getAllSubmission(UserPrincipal user, Long contestId, Pageable pageable);
}
