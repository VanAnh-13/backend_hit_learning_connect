package com.example.projectbase.controller;


import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.constant.ResponseMessage;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.contest.ContestCreatetDto;
import com.example.projectbase.domain.dto.request.contest.ContestSubmissionRequest;
import com.example.projectbase.domain.dto.request.contest.ContestSubmissionRequestWrapper;
import com.example.projectbase.domain.dto.request.contest.ContestUpdateDto;
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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RestApiV1
@RequiredArgsConstructor
@Validated
public class ContestController {
    private final ContestService service;

    @Operation(summary = "Api get all contest")
    @GetMapping(UrlConstant.Contest.GET_ALL_CONTEST)
    public ResponseEntity<?> getAllPaged(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        try {
            return VsResponseUtil.success(service.getAllPaged(page, size));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Api search contest")
    @GetMapping(UrlConstant.Contest.SEARCH_CONTEST)
    public ResponseEntity<?> search(@RequestParam String keyword,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        try {
            return ResponseEntity.ok(service.search(keyword, page, size));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "view details contest by id")
    @GetMapping(UrlConstant.Contest.GET_CONTEST)
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ContestResponseDto> createContest(
            @Parameter(description = "Contest data", required = true, content = @Content(schema = @Schema(implementation = ContestCreatetDto.class)))
            @RequestPart("request") @Valid ContestCreatetDto request,

            @Parameter(description = "PDF file", required = true, content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
            @RequestPart("file") MultipartFile file) {

        ContestResponseDto response = service.createContest(request, file);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Api update contest by id")
    @PutMapping(UrlConstant.Contest.GET_CONTEST)
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
    @DeleteMapping(UrlConstant.Contest.GET_CONTEST)
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
    @GetMapping(UrlConstant.Contest.GET_RESULT_CONTEST)
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

    @Operation(summary = "Api View exam details")
    @GetMapping(UrlConstant.Contest.GET_CONTEST)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getContestDetail(@PathVariable Long id){
        try{
            ContestResponseDto response= service.getById(id);
            return ResponseEntity.ok(response);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Contest.CONTEST_DETAIL_NOT_FOUND);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "API join contest by id")
    @PostMapping(UrlConstant.Contest.JOIN_CONTEST)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> joinContest(@PathVariable Long contestId, @Parameter(name = "principal", hidden = true)
                                         @CurrentUser UserPrincipal principal
    ){
        service.joinContest(contestId,principal);
        return VsResponseUtil.success(ResponseMessage.Contest.JOIN_CONTEST);
    }

    /***
     *
     * @param
     * @return
     */
    @Operation(summary = "Api start contest")
    @GetMapping(UrlConstant.Contest.START_CONTEST)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> startContest(@PathVariable Long contestId) {
        try {
            return ResponseEntity.ok(service.startContest(contestId));
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

    @PostMapping(value = UrlConstant.Contest.SUBMIT_CONTEST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> submitContest(
            @RequestPart("request") String requestJson,
            @RequestPart("file") MultipartFile file,
            @CurrentUser UserPrincipal user
    ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ContestSubmissionRequest request = mapper.readValue(requestJson, ContestSubmissionRequest.class);
        return ResponseEntity.ok(ResponseMessage.Contest.SUBMIT_FILE);
    }
}
