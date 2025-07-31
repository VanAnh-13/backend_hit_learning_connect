package com.example.projectbase.controller.contest;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.constant.ResponseMessage;
import com.example.projectbase.domain.dto.request.contest.ContestSubmissionRequest;
import com.example.projectbase.domain.dto.request.contest.ContestSubmissionRequestWrapper;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RequestMapping("api/contest")
@RestController
@RequiredArgsConstructor
@Validated

public class ContestMemberController {

    private final ContestService contestService;


    @Operation(summary = "Api get all contest ")
    @GetMapping("/page")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllPaged(@RequestParam(defaultValue ="0" ) int page,
                                         @RequestParam(defaultValue = "10") int size){
      try{
          return ResponseEntity.ok(contestService.getAllPaged(page, size));
      }catch (Exception e){
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
      }
    }

    @Operation(summary = "Api search contest")
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> search(@RequestParam String keywork,@RequestParam (defaultValue = "0") int page,
                                    @RequestParam (defaultValue = "10")int size){
        try{
            Page<ContestResponseDto> contestPage= contestService.search(keywork, page, size);
            return ResponseEntity.ok(contestPage);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Api View exam details")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getContestDetail(@PathVariable Long id){
        try{
            ContestResponseDto reponse= contestService.getById(id);
            return ResponseEntity.ok(reponse);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Contest.CONTEST_DETAIL_NOT_FOUND);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

//    @Tags({
//            @Tag(name = "contest-MEMBER-controller")
//    })
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "API join contest by id")
    @PostMapping("/join/{contestId}")
    public ResponseEntity<?> joinContest(@PathVariable Long contestId, @Parameter(name = "principal", hidden = true)
    @CurrentUser UserPrincipal principal
    ){
        contestService.joinContest(contestId,principal);
        return VsResponseUtil.success(ResponseMessage.Contest.JOIN_CONTEST);
    }

    /***
     *
     * @param
     * @return
     */
    @Operation(summary = "Api start contest")
    @GetMapping("/start/{contestId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> startContest(@PathVariable Long contestId) {
        try {
            return ResponseEntity.ok(contestService.startContest(contestId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.Contest.CONTEST_TIME_INVALID);
        } catch (EntityNotFoundException e) {
            String message = e.getMessage();
            if (ErrorMessage.Contest.USER_CONTEST_NOT_FOUND.equals(message)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Contest.USER_CONTEST_NOT_FOUND);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Contest.CONTEST_NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/contest/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Submit contest file",
            description = "submit the entry for the competition",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = ContestSubmissionRequestWrapper.class)
                    )
            )
    )
    public ResponseEntity<?> submitContest(
            @RequestPart("request") String requestJson,
            @RequestPart("file") MultipartFile file,
            @CurrentUser UserPrincipal user
    ) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        ContestSubmissionRequest request = mapper.readValue(requestJson, ContestSubmissionRequest.class);
        contestService.submitContest(request, file);
        return ResponseEntity.ok(ResponseMessage.Contest.SUBMIT_FILE);
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
