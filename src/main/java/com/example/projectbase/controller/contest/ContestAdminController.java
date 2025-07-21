package com.example.projectbase.controller.contest;

import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.dto.request.contest.ContestCreatetDto;
import com.example.projectbase.domain.dto.request.contest.ContestUpdateDto;
import com.example.projectbase.domain.dto.response.contest.ContestReponseDto;
import com.example.projectbase.domain.dto.response.contest.ContestResultResponse;
import com.example.projectbase.service.ContestService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/contests")
@RequiredArgsConstructor
@Validated

public class ContestAdminController {

    private final ContestService service;

    @Operation(summary = "Api get all contest")
    @GetMapping("/paged")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    public ResponseEntity<?> getAllPaged(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam (defaultValue = "10") int size){
        try{
            return ResponseEntity.ok(service.getAllPaged(page, size));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);

        }
    }

    @Operation(summary = "Api search contest")
   @GetMapping("/search")
   @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    public ResponseEntity<?> search(@RequestParam String keywork,
                                    @RequestParam (defaultValue = "0") int page,
                                    @RequestParam (defaultValue = "10") int size){
        try{
            return ResponseEntity.ok(service.search(keywork,page,size));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Contest.INTERNAL_SERVER_ERROR);
        }
   }

   @Operation(summary = "view details contest by id")
   @GetMapping("/{id}")
   @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
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

   @Operation(summary = "Api create contest")
   @PostMapping
   @PreAuthorize("hasRole('ADMIN')")
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

   @Operation(summary = "Api update contest by id")
   @PutMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
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

   @Operation(summary = "Api delete contest by id")
   @DeleteMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
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

}
