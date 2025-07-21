package com.example.projectbase.controller.contest;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.dto.response.contest.ContestResultResponse;
import com.example.projectbase.service.ContestService;
import com.example.projectbase.service.impl.ContestServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;


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

//    @Operation(summary = "Api se")
    @Operation(summary = "Api result contest by id")
    @GetMapping("{id}/result")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getResultByContestId(@PathVariable Long id){

        try{
            ContestResultResponse resultResponse= contestService.getResultByContestId(id);
            return ResponseEntity.ok(resultResponse);
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
}
