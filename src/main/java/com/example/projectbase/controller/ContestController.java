package com.example.projectbase.controller;

import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.dto.response.contest.ContestReponseDto;
import com.example.projectbase.service.ContestService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/contests")
@RequiredArgsConstructor
@Validated

public class ContestController {

    private final ContestService service;

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

   
}
