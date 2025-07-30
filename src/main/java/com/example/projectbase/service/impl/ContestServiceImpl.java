package com.example.projectbase.service.impl;

import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.dto.request.contest.ContestCreatetDto;
import com.example.projectbase.domain.dto.request.contest.ContestSubmissionRequest;
import com.example.projectbase.domain.dto.request.contest.ContestUpdateDto;
import com.example.projectbase.domain.dto.response.contest.ContestResponseDto;
import com.example.projectbase.domain.dto.response.contest.ContestResponseDto;
import com.example.projectbase.domain.dto.response.contest.ContestResultResponse;
import com.example.projectbase.domain.entity.Contest;
import com.example.projectbase.domain.entity.ContestSubmission;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.mapper.ContestMapper;
import com.example.projectbase.exception.extended.InternalServerException;
import com.example.projectbase.repository.ContestRepository;
import com.example.projectbase.repository.ContestSubmissionRepository;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.ContestService;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static final String CACHE_PREFIX="contest:";

    @Override
    public Page<ContestResponseDto> getAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").descending());
        return contestRepository.findAll(pageable).map(mapper::toReponse);

    }

    @Override
    public Page<ContestResponseDto> search(String keyword, int page, int size) {
      Pageable pageable=PageRequest.of(page, size, Sort.by("startTime").descending());
        return contestRepository.findByTitleContainingIgnoreCase(keyword, pageable).map(mapper:: toReponse)
                ;
    }

    @Override
    public ContestResponseDto getById(Long id) {
        Contest contest= contestRepository.findById(id).orElseThrow(()->new RuntimeException(ErrorMessage.Contest.CONTEST_NOT_FOUND));
        return mapper.toReponse(contest);
    }

    @Override
    public ContestResponseDto createContest(ContestCreatetDto request, MultipartFile file) {
        Contest contest = mapper.toEntity(request);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ERR_NOT_FOUND));

        contest.setCreatedBy(user);

        try {
            if (file == null || file.isEmpty()) {
                throw new RuntimeException(ErrorMessage.Contest.CONTEST_NOT_FOUND);
            }

            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isBlank()) {
                throw new RuntimeException(ErrorMessage.Contest.ORIGINAL_FILENAME);
            }

            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", "contest");
            Files.createDirectories(uploadPath);

            String fileName = System.currentTimeMillis() + "_" + originalFileName;
            Path filePath = uploadPath.resolve(fileName);

            file.transferTo(filePath.toFile());

            contest.setFileName(originalFileName);
            contest.setFileUrl(filePath.toAbsolutePath().toString());

        } catch (IOException e) {
            log.error(ErrorMessage.Contest.UPLOADING_FILE, e);
            throw new RuntimeException(ErrorMessage.Contest.FILE_UPLOAD_FAILED, e);
        } catch (RuntimeException e) {
            log.error(ErrorMessage.Contest.VALIDATION_FAILED, e);
            throw e;
        }

        return mapper.toReponse(contestRepository.save(contest));
    }

    @Override
    public ContestResponseDto updateContest(Long id, ContestUpdateDto request) {
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

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username= authentication.getName();

        boolean isAdminOrLeader = authentication.getAuthorities().stream().anyMatch(role-> role.getAuthority().equals("ROLE_ADMIN") || role.getAuthority().equals("ROLE_LEADER"));

        Contest contest= contestRepository.findById(contestId).orElseThrow(()-> new EntityNotFoundException(ErrorMessage.Contest.CONTEST_NOT_FOUND));

        if(contest.getStartTime() !=null && contest.getEndTime() != null && contest.getEndTime().isBefore(contest.getStartTime())){
            throw new IllegalStateException(ErrorMessage.Contest.CONTEST_TIME_INVALID);
        }

        if(!isAdminOrLeader){
            if(contest.getEndTime().isAfter(LocalDateTime.now())){
                throw new IllegalArgumentException(ErrorMessage.Contest.CONTEST_RESULT_NOT_AVAILABLE);
            }

            boolean hasContestSubmission= submissionRepository.existsByContest_ContestIdAndCreatedBy_Username(contestId,username);
            if(!hasContestSubmission){
                throw new IllegalArgumentException(ErrorMessage.Contest.USER_CONTEST_NOT_FOUND);
            }
        }
        return mapper.toResultResponse(contest);
    }

    @Override
    @Transactional
    public void joinContest(Long contestId, UserPrincipal userPrincipal) {
        Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new RuntimeException(ErrorMessage.Contest.CONTEST_NOT_FOUND));
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new RuntimeException(ERR_NOT_FOUND));


        for (User participant : contest.getParticipants()) {
            System.out.println(participant);
        }

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
    public void submitContest(ContestSubmissionRequest request, MultipartFile file) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ERR_NOT_FOUND));

        ContestSubmission contestSubmission = submissionRepository
                .findByContest_ContestIdAndCreatedBy_Username(request.getContestId(), username)
                .orElseThrow(() -> new EntityNotFoundException(ERR_NOT_FOUND));

        String uploadDir = "uploads/contest/";
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File targetFile = new File(uploadDir + fileName);

        targetFile.getParentFile().mkdirs();

        file.transferTo(targetFile);

        contestSubmission.setFileName(file.getOriginalFilename());
        contestSubmission.setFileUrl(targetFile.getAbsolutePath());
        contestSubmission.setSubmittedAt(LocalDateTime.now());

        submissionRepository.save(contestSubmission);
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
}