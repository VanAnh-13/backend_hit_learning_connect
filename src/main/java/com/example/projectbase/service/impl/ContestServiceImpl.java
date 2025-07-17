package com.example.projectbase.service.impl;

import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.dto.request.contest.ContestCreatetDto;
import com.example.projectbase.domain.dto.request.contest.ContestUpdateDto;
import com.example.projectbase.domain.dto.response.contest.ContestReponseDto;
import com.example.projectbase.domain.dto.response.contest.ContestResultResponse;
import com.example.projectbase.domain.entity.Contest;
import com.example.projectbase.domain.mapper.ContestMapper;
import com.example.projectbase.repository.ContestRepository;
import com.example.projectbase.service.ContestService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class ContestServiceImpl implements ContestService {

    private final ContestRepository contestRepository;
    private final ContestMapper mapper;

    @Override
    public Page<ContestReponseDto> getAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").descending());
        return contestRepository.findAll(pageable).map(mapper::toReponse);

    }

    @Override
    public Page<ContestReponseDto> search(String keyword, int page, int size) {
      Pageable pageable=PageRequest.of(page, size, Sort.by("startTime").descending());
        return contestRepository.findByTitleContainingIgnoreCase(keyword, pageable).map(mapper::toReponse)
                ;
    }

    @Override
    public ContestReponseDto getById(Long id) {
        Contest contest= contestRepository.findById(id).orElseThrow(()->new RuntimeException(ErrorMessage.Contest.CONTEST_NOT_FOUND));
        return mapper.toReponse(contest);
    }

    @Override
    public ContestReponseDto createContest(ContestCreatetDto request) {
        Contest contest= mapper.toEntity(request);
        return mapper.toReponse(contestRepository.save(contest));
    }

    @Override
    public ContestReponseDto updateContest(Long id, ContestUpdateDto request) {
     Contest contest= contestRepository.findById(id).orElseThrow(()->new RuntimeException(ErrorMessage.Contest.CONTEST_NOT_FOUND));
     mapper.updateEntity(contest, request);
        return mapper.toReponse(contestRepository.save(contest));
    }

    @Override
    public void deleteContest(Long id) {
        if(!contestRepository.existsById(id)){
            throw new RuntimeException(ErrorMessage.Contest.CONTEST_NOT_FOUND);
        }
        contestRepository.deleteById(id);
    }

    @Override
    public ContestResultResponse getResultByContestId(Long contestId) {
        Contest contest= contestRepository.findById(contestId).orElseThrow(()-> new EntityNotFoundException(ErrorMessage.Contest.CONTEST_NOT_FOUND));

        if(contest.getStartTime() !=null && contest.getEndTime() != null && contest.getEndTime().isBefore(contest.getStartTime())){
            throw new IllegalStateException(ErrorMessage.Contest.CONTEST_TIME_INVALID);
        }
        return mapper.toResultResponse(contest);
    }
}
