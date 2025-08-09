package com.example.projectbase.service.impl;

import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.dto.request.contest.ContestCreatetDto;
import com.example.projectbase.domain.dto.request.contest.ContestSubmissionRequest;
import com.example.projectbase.domain.dto.request.contest.ContestUpdateDto;
import com.example.projectbase.domain.dto.request.contest.ScoringRequest;
import com.example.projectbase.domain.dto.response.contest.ContestResponseDto;
import com.example.projectbase.domain.dto.response.contest.ContestResultResponse;
import com.example.projectbase.domain.dto.response.contest.ContestSubmissionResponse;
import com.example.projectbase.domain.dto.response.storage.UploadFileResponseDto;
import com.example.projectbase.domain.entity.Contest;
import com.example.projectbase.domain.entity.ContestProblem;
import com.example.projectbase.domain.entity.ContestSubmission;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.mapper.ContestMapper;
import com.example.projectbase.domain.model.SubmissionStatus;
import com.example.projectbase.exception.extended.InternalServerException;
import com.example.projectbase.exception.extended.NotFoundException;
import com.example.projectbase.exception.extended.UploadFileException;
import com.example.projectbase.repository.ContestRepository;
import com.example.projectbase.repository.ContestSubmissionRepository;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.ContestService;
import com.example.projectbase.service.StorageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.projectbase.constant.ErrorMessage.User.ERR_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ContestServiceImpl implements ContestService {

    private static final Logger log = LoggerFactory.getLogger(ContestServiceImpl.class);

    private final ContestRepository contestRepository;
    private final ContestMapper mapper;
    private final UserRepository userRepository;
    private final ContestSubmissionRepository submissionRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final StorageService service;

    private static final String CACHE_PREFIX = "contest:";
    private final ContestSubmissionRepository contestSubmissionRepository;

    @Override
    public Page<ContestResponseDto> getAll(Pageable pageable) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException(ERR_NOT_FOUND));

        Page<Contest> contestPage = contestRepository.findAll(pageable);

        return contestPage.map(contest -> {
            boolean hasJoined = contestRepository
                    .hasUserJoinedContest(contest.getContestId(), user.getId());

            ContestResponseDto response = mapper.toReponse(contest);
            response.setHasJoined(hasJoined);
            return response;
        });
    }


    @Override
    public Page<ContestResponseDto> search(String keyword, Pageable pageable) {
        return contestRepository.findByTitle(keyword, pageable).map(mapper::toReponse);
    }

    @Override
    public ContestResponseDto getById(Long id) {
        Contest contest = contestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ErrorMessage.Contest.CONTEST_NOT_FOUND));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException(ERR_NOT_FOUND));

        boolean hasJoined = contestRepository.hasUserJoinedContest(contest.getContestId(), user.getId());

        ContestResponseDto dto = mapper.toReponse(contest);
        dto.setHasJoined(hasJoined);

        return dto;
    }

    @Override
    public ContestResponseDto createContest(ContestCreatetDto request) {
        Contest contest = mapper.toEntity(request);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException(ERR_NOT_FOUND));

        contest.setCreatedBy(user);

        if (request.getUrlFile() == null || request.getUrlFile().isBlank()) {
            throw new RuntimeException(ErrorMessage.Contest.FILE_URL_REQUIRED);
        }

        contest.setFileName("UploadedViaOtherWay.pdf");
        contest.setFileUrl(request.getUrlFile());

        return mapper.toReponse(contestRepository.save(contest));
    }


    @Override
    public ContestResponseDto updateContest(Long id, ContestUpdateDto request) {
        Contest contest = contestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ErrorMessage.Contest.CONTEST_NOT_FOUND));
        mapper.updateEntity(contest, request);
        return mapper.toReponse(contestRepository.save(contest));
    }

    @Override
    public void deleteContest(Long id) {
        if (!contestRepository.existsById(id)) {
            throw new RuntimeException(ErrorMessage.Contest.CONTEST_NOT_FOUND);
        }
        contestRepository.deleteById(id);
    }

    @Override
    public ContestResultResponse getResultByContestId(Long contestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean isAdminOrLeader = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN") || role.getAuthority().equals("ROLE_LEADER"));

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.Contest.CONTEST_NOT_FOUND));

        if (contest.getStartTime() != null && contest.getEndTime() != null &&
                contest.getEndTime().isBefore(contest.getStartTime())) {
            throw new IllegalStateException(ErrorMessage.Contest.CONTEST_TIME_INVALID);
        }

        if (!isAdminOrLeader) {
            if (contest.getEndTime().isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException(ErrorMessage.Contest.CONTEST_RESULT_NOT_AVAILABLE);
            }

            boolean hasSubmission = submissionRepository.existsByContest_ContestIdAndCreatedBy_Username(contestId, username);
            if (!hasSubmission) {
                throw new IllegalArgumentException(ErrorMessage.Contest.USER_CONTEST_NOT_FOUND);
            }
        }

        return mapper.toResultResponse(contest, username);
    }

    @Override
    @Transactional
    public void joinContest(Long contestId, UserPrincipal userPrincipal) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new RuntimeException(ErrorMessage.Contest.CONTEST_NOT_FOUND));

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException(ERR_NOT_FOUND));

        if (!contestRepository.existsParticipant(contestId, user.getUsername())) {
            contest.getParticipants().add(user);
            contestRepository.save(contest);
        } else {
            throw new InternalServerException(ErrorMessage.Contest.ALREADY_JOINED);
        }
    }

    @Override
    public ContestResponseDto startContest(Long contestId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.Contest.CONTEST_NOT_FOUND));

        boolean isParticipant = contestRepository.existsParticipant(contestId, username);
        if (!isParticipant) {
            throw new EntityNotFoundException(ErrorMessage.Contest.USER_CONTEST_NOT_FOUND);
        }

        if (LocalDateTime.now().isBefore(contest.getStartTime()) || LocalDateTime.now().isAfter(contest.getEndTime())) {
            throw new IllegalArgumentException(ErrorMessage.Contest.CONTEST_TIME_INVALID);
        }

        return mapper.toReponse(contest);
    }

    @Override
    public ContestSubmissionResponse submitContest(String urlFile, Long contestId, UserPrincipal userPrincipal) throws IOException {

        Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new NotFoundException(ErrorMessage.Contest.CONTEST_NOT_FOUND));

        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new RuntimeException(ERR_NOT_FOUND));

        ContestSubmission contestSubmission = ContestSubmission.builder()
                .contest(contest)
                .createdBy(user)
                .fileUrl(urlFile)
                .fileName("none")
                .status(SubmissionStatus.PENDING)
                .ranking("none")
                .highestScore(0)
                .code("None")
                .resultSummary("Wait for the scorer")
                .build();

        ContestSubmission savedContestSubmisstion = contestSubmissionRepository.save(contestSubmission);


        return toContestSubmissionResponse(savedContestSubmisstion);
    }

    @Scheduled(cron = "0 0 * * * *")
    @Override
    public void autoCloseExpiredContests() {
        List<Contest> contests = contestRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (Contest contest : contests) {
            if (contest.getEndTime().isBefore(now) && !Boolean.TRUE.equals(contest.getStartTime())) {
                contest.setStartTime(now);
                contestRepository.save(contest);
            }
        }
    }

    @Override
    public Page<ContestSubmissionResponse> getAllSubmission(UserPrincipal user, Long contestId, Pageable pageable) {
        return contestSubmissionRepository.findAllSubmission(contestId, user.getId(), pageable).map(this::toContestSubmissionResponse);
    }

    @Override
    public Page<ContestSubmissionResponse> getAllSubmissionNoUser(Long contestId, Pageable pageable) {
        return contestSubmissionRepository.findAllSubmissionNoUser(contestId, pageable).map(this::toContestSubmissionResponse);
    }

    @Override
    public ContestSubmissionResponse scoringSubmission(Long submissionId, ScoringRequest scoringRequest) {
        ContestSubmission contestSubmission = contestSubmissionRepository.findById(submissionId).orElseThrow(
                () -> new RuntimeException(ErrorMessage.ContestSubmission.SUBMISSION_NOT_FOUND)
        );

        contestSubmission.setHighestScore(scoringRequest.getScore());
        contestSubmission.setResultSummary(scoringRequest.getResultSummary());

        contestSubmissionRepository.save(contestSubmission);

        return toContestSubmissionResponse(contestSubmission);
    }

    public ContestSubmissionResponse toContestSubmissionResponse(ContestSubmission contestSubmission) {
        return ContestSubmissionResponse.builder()
                .contestId(contestSubmission.getSubmissionId())
                .fileUrl(contestSubmission.getFileUrl())
                .highestScore(contestSubmission.getHighestScore())
                .ranking(contestSubmission.getRanking())
                .resultSummary(contestSubmission.getResultSummary())
                .username(contestSubmission.getCreatedBy().getUsername())
                .build();
    }
}
