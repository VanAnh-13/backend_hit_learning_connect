package com.example.projectbase.controller.contest;

import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.constant.ResponseMessage;
import com.example.projectbase.domain.dto.request.contest.ContestCreatetDto;
import com.example.projectbase.domain.dto.request.contest.ContestUpdateDto;
import com.example.projectbase.domain.dto.request.contest.ScoringRequest;
import com.example.projectbase.domain.dto.response.contest.ContestResponseDto;
import com.example.projectbase.domain.dto.response.contest.ContestResultResponse;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.ContestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/contests")
@RequiredArgsConstructor
@Validated

public class ContestAdminController {

    private final ContestService service;

    @Operation(summary = "Api get all contest")
    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    public ResponseEntity<?> getAllPaged(@ParameterObject @PageableDefault(page = 0, size = 1000, sort = "contestId", direction = Sort.Direction.ASC) Pageable pageable) {
        try {
            return VsResponseUtil.success(service.getAll(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);

        }
    }


    @Operation(summary = "Api search contest")
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    public ResponseEntity<?> search(@RequestParam String keyword,
                                    @ParameterObject @PageableDefault(page = 0, size = 1000, sort = "contestId", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.ok(service.search(keyword, pageable));
    }

    @Operation(summary = "view details contest by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            ContestResponseDto reponse = service.getById(id);
            return ResponseEntity.ok(reponse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Contest.CONTEST_DETAIL_NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create contest with file upload")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Created")
    })
    @PostMapping("")
    public ResponseEntity<ContestResponseDto> createContest(
            @RequestBody ContestCreatetDto request
            ) {

        ContestResponseDto response = service.createContest(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Api update contest by id")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateContest(@PathVariable Long id, @Valid @RequestBody ContestUpdateDto request) {
        try {
            return ResponseEntity.ok(service.updateContest(id, request));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Contest.CONTEST_NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Api delete contest by id")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteContest(@PathVariable Long id) {
        try {
            service.deleteContest(id);
            return VsResponseUtil.success(ResponseMessage.DELETE_SUCCESS);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Contest.CONTEST_NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Api result contest by id")
    @GetMapping("/{id}/result")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    public ResponseEntity<?> getResultByContestId(@PathVariable Long id) {
        try {
            ContestResultResponse result = service.getResultByContestId(id);
            return ResponseEntity.ok(result);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Contest.CONTEST_NOT_FOUND);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals(ErrorMessage.Contest.CONTEST_RESULT_NOT_AVAILABLE)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.Contest.CONTEST_RESULT_NOT_AVAILABLE);
            } else if (e.getMessage().equals(ErrorMessage.Contest.CONTEST_TIME_INVALID)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.Contest.CONTEST_TIME_INVALID);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Api get all submission by contest id")
    @PostMapping(value = "/submission/{contestId}")
    public ResponseEntity<?> getSubmissions(
            @PathVariable Long contestId,
            @ParameterObject @PageableDefault(page = 0, size = 1000) Pageable pageable) {
        return ResponseEntity.ok(service.getAllSubmissionNoUser(contestId, pageable));
    }

    @Operation(summary = "Api scoring for submissionId")
    @PostMapping(value = "/scoring/{submissionId}")
    public ResponseEntity<?> scoring(
            @PathVariable Long submissionId,
            @RequestBody ScoringRequest scoringRequest
            ) {
        return ResponseEntity.ok(service.scoringSubmission(submissionId, scoringRequest));
    }


}
