package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.contest.ContestCreatetDto;
import com.example.projectbase.domain.dto.request.contest.ContestUpdateDto;
import com.example.projectbase.domain.dto.response.contest.ContestReponseDto;
import com.example.projectbase.domain.dto.response.contest.ContestResultResponse;
import com.example.projectbase.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContestService {
    Page<ContestReponseDto> getAll(Pageable pageable);
    Page<ContestReponseDto> search(String keyword, Pageable pageable);
    ContestReponseDto getById(Long id);
    ContestReponseDto createContest(ContestCreatetDto request);;
    ContestReponseDto updateContest(Long id, ContestUpdateDto request);
    void deleteContest(Long id);
    ContestResultResponse  getResultByContestId(Long contestId);
    void joinContest(Long contestId, UserPrincipal userPrincipal);

}
