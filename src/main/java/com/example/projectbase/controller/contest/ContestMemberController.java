package com.example.projectbase.controller.contest;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.constant.ResponseMessage;
import com.example.projectbase.domain.dto.request.contest.ContestSubmissionRequest;
import com.example.projectbase.domain.dto.request.contest.ContestSubmissionRequestWrapper;
import com.example.projectbase.domain.dto.request.storage.UrlFileRequest;
import com.example.projectbase.domain.dto.response.contest.ContestResponseDto;
import com.example.projectbase.domain.dto.response.contest.ContestResultResponse;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.ContestService;
import com.example.projectbase.service.impl.ContestServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RequestMapping("api/v1/contest")
@RestController
@RequiredArgsConstructor
@Validated

public class ContestMemberController {

    private final ContestService contestService;


    @Operation(summary = "Api get all contest ")
    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllPaged(@ParameterObject @PageableDefault(page = 0, size = 1000, sort = "contestId", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(contestService.getAll(pageable));
    }

    @Operation(summary = "Api search contest")
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> search(@RequestParam String keyword,
                                    @ParameterObject @PageableDefault(page = 0, size = 1000, sort = "contestId", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.ok(contestService.search(keyword, pageable));
    }

    @Operation(summary = "Api View exam details")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getContestDetail(@PathVariable Long id){
        return ResponseEntity.ok(contestService.getById(id));
    }


    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "API join contest by id")
    @PostMapping("/join/{contestId}")
    public ResponseEntity<?> joinContest(@PathVariable Long contestId, @Parameter(name = "principal", hidden = true)
    @CurrentUser UserPrincipal principal
    ){
        contestService.joinContest(contestId, principal);
        return VsResponseUtil.success(ResponseMessage.Contest.JOIN_CONTEST);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Api submit contest file")
    @PostMapping(value = "/submit/{contestId}")
    public ResponseEntity<?> submitContest(
            @PathVariable Long contestId,
            @RequestBody UrlFileRequest urlFile,
            @Parameter(name = "principal", hidden = true) @CurrentUser UserPrincipal user
    ) throws IOException {
        return ResponseEntity.ok(contestService.submitContest(urlFile.getUrlFile(), contestId, user));
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Api get all submission by contest id")
    @PostMapping(value = "/submission/{contestId}")
    public ResponseEntity<?> getSubmissions(
            @PathVariable Long contestId,
            @ParameterObject @PageableDefault(page = 0, size = 1000) Pageable pageable,
            @Parameter(name = "principal", hidden = true) @CurrentUser UserPrincipal user) {
        return ResponseEntity.ok(contestService.getAllSubmission(user, contestId, pageable));
    }

    @Operation(summary = "Api result contest by id")
    @GetMapping("{id}/result")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getResultByContestId(@PathVariable Long id) {
        try {
            ContestResultResponse resultResponse = contestService.getResultByContestId(id);
            return ResponseEntity.ok(resultResponse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Contest.CONTEST_NOT_FOUND);
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            if (msg.equals(ErrorMessage.Contest.CONTEST_RESULT_NOT_AVAILABLE)) {
                return ResponseEntity.badRequest().body(ErrorMessage.Contest.CONTEST_RESULT_NOT_AVAILABLE);
            } else if (msg.equals(ErrorMessage.Contest.USER_CONTEST_NOT_FOUND)) {
                return ResponseEntity.badRequest().body(ErrorMessage.Contest.USER_CONTEST_NOT_FOUND);
            } else if (msg.equals(ErrorMessage.Contest.CONTEST_TIME_INVALID)) {
                return ResponseEntity.badRequest().body(ErrorMessage.Contest.CONTEST_TIME_INVALID);
            }
            return ResponseEntity.badRequest().body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

}