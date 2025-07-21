package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.contest.ContestCreatetDto;
import com.example.projectbase.domain.dto.request.contest.ContestUpdateDto;
import com.example.projectbase.domain.dto.response.contest.ContestReponseDto;
import com.example.projectbase.domain.dto.response.contest.ContestResultResponse;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.ContestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RestApiV1
@RequiredArgsConstructor
@Validated

public class ContestController {

    private final ContestService service;

    @Tags({
            @Tag(name = "contest-MEMBER-controller"),
            @Tag(name = "contest-ADMIN-controller")
    })
    @Operation(summary = "Api get all contest")
    @GetMapping(UrlConstant.Contest.BASE)
    public ResponseEntity<?> getAll(@ParameterObject Pageable pageable){
        try{
            return ResponseEntity.ok(service.getAll(pageable));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

    @Tags({
            @Tag(name = "contest-MEMBER-controller"),
            @Tag(name = "contest-ADMIN-controller")
    })
    @Operation(summary = "Api search contest")
    @GetMapping("/search")
    public ResponseEntity<?> search(@ParameterObject @PageableDefault(page = 0, size = 100, sort = "timestamp", direction = Sort.Direction.ASC)
                                        Pageable pageable,
                                    @RequestParam String keyword){
        try{
            return ResponseEntity.ok(service.search(keyword ,pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

    @Tags({
            @Tag(name = "contest-MEMBER-controller"),
            @Tag(name = "contest-ADMIN-controller")
    })
    @Operation(summary = "Api get contest by id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        try{
            ContestReponseDto reponse= service.getById(id);
            return ResponseEntity.ok(reponse);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Contest.CONTEST_DETAIL_NOT_FOUND);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

    @Tags({
            @Tag(name = "contest-ADMIN-controller")
    })
    @Operation(summary = "Api create contest")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    public ResponseEntity<?> createContest(@Valid @RequestBody ContestCreatetDto request){
        try{
            ContestReponseDto create= service.createContest(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(create);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

    @Tags({
            @Tag(name = "contest-ADMIN-controller")
    })
    @Operation(summary = "Api update contest by id")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    public ResponseEntity<?> updateContest(@PathVariable Long id, @Valid @RequestBody ContestUpdateDto request) {
        try {
            return ResponseEntity.ok(service.updateContest(id,request));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Contest.CONTEST_NOT_FOUND);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

    @Tags({
            @Tag(name = "contest-ADMIN-controller")
    })
    @Operation(summary = "Api delete contest by id")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    public ResponseEntity<?> deleteContest(@PathVariable Long id){
        try{
            service.deleteContest(id);
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Contest.CONTEST_NOT_FOUND);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

    @Tags({
            @Tag(name = "contest-ADMIN-controller")
    })
    @Operation(summary = "Api result contest by id")
    @GetMapping("/{id}/result")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    public ResponseEntity<?> getResultByContestId(@PathVariable Long id){
        try{
            ContestResultResponse result=service.getResultByContestId(id);
            return ResponseEntity.ok(result);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Contest.CONTEST_NOT_FOUND);
        }catch (IllegalArgumentException e){
            if(e.getMessage().equals(ErrorMessage.Contest.CONTEST_RESULT_NOT_AVAILABLE)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.Contest.CONTEST_RESULT_NOT_AVAILABLE);
            } else if (e.getMessage().equals(ErrorMessage.Contest.CONTEST_TIME_INVALID)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.Contest.CONTEST_TIME_INVALID);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
    }

    @Tags({
            @Tag(name = "contest-MEMBER-controller")
    })
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "API join contest by id")
    @PostMapping("/join/{contestId}")
    public ResponseEntity<?> joinContest(@PathVariable Long contestId, @Parameter(name = "principal", hidden = true)
                                         @CurrentUser UserPrincipal principal
                                         ){
        service.joinContest(contestId,principal);
        return VsResponseUtil.success("Join contest successfully");
    }

}
