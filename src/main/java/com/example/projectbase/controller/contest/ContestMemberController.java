package com.example.projectbase.controller.contest;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.constant.ResponseMessage;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.contest.ContestSubmissionRequest;
import com.example.projectbase.domain.dto.request.contest.ContestSubmissionRequestWrapper;
import com.example.projectbase.domain.dto.response.contest.ContestResponseDto;
import com.example.projectbase.domain.dto.response.contest.ContestResultResponse;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.ContestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestApiV1
@RestController
@RequiredArgsConstructor
@Validated
public class ContestMemberController {

    private final ContestService contestService;














    @Operation(summary = "Api result contest by id")
    @GetMapping(UrlConstant.Contest.GET_RESULT_CONTEST)
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
