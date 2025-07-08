package com.hit.leaning_connect.controller;

import com.hit.leaning_connect.domain.reponse.ApiResponse;
import com.hit.leaning_connect.domain.reponse.ClassEvaluationResponseDto;
import com.hit.leaning_connect.domain.reponse.ClassResponseDto;
import com.hit.leaning_connect.domain.reponse.DocumentResponseDto;
import com.hit.leaning_connect.domain.request.ClassEvaluationRequestDto;
import com.hit.leaning_connect.domain.request.DocumentRequestDto;
import com.hit.leaning_connect.service.ClassEvaluationService;
import com.hit.leaning_connect.service.ClassService;
import com.hit.leaning_connect.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final ClassService classService;
    private final DocumentService documentService;
    private final ClassEvaluationService evaluationService;
    
    // Helper method to get the current authenticated user ID
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Long.parseLong(userDetails.getUsername());
    }
    
    // Find classes (search or list all)
    @GetMapping("/classes")
    public ResponseEntity<ApiResponse<List<ClassResponseDto>>> findClasses(
            @RequestParam(required = false) String query) {
        List<ClassResponseDto> classes = classService.getALlClass();
        return ResponseEntity.ok(new ApiResponse<>(true, "Classes found successfully", classes));
    }
    
    // Get class details by ID
    @GetMapping("/classes/{id}")
    public ResponseEntity<ApiResponse<ClassResponseDto>> getClassById(@PathVariable Long id) {
        ClassResponseDto classDto = classService.getClassById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Class details retrieved successfully", classDto));
    }
    
    // View all documents
    @GetMapping("/documents")
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> getAllDocuments() {
        List<DocumentResponseDto> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(new ApiResponse<>(true, "Documents retrieved successfully", documents));
    }
    
    // View document by ID
    @GetMapping("/documents/{id}")
    public ResponseEntity<ApiResponse<DocumentResponseDto>> getDocumentById(@PathVariable Long id) {
        DocumentResponseDto document = documentService.getDocumentById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Document retrieved successfully", document));
    }
    
    // Upload a new document
    @PostMapping("/documents")
    public ResponseEntity<ApiResponse<DocumentResponseDto>> uploadDocument(
            @Valid @RequestBody DocumentRequestDto documentRequestDto) {
        Long currentUserId = getCurrentUserId();
        DocumentResponseDto document = documentService.addDocument(documentRequestDto, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Document uploaded successfully", document));
    }
    
    // Send evaluation for a class
    @PostMapping("/evaluations")
    public ResponseEntity<ApiResponse<ClassEvaluationResponseDto>> evaluateClass(
            @Valid @RequestBody ClassEvaluationRequestDto evaluationRequestDto) {
        Long currentUserId = getCurrentUserId();
        ClassEvaluationResponseDto evaluation = evaluationService.addEvaluation(evaluationRequestDto, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Class evaluation submitted successfully", evaluation));
    }
    
    // Get all evaluations made by the current user
    @GetMapping("/evaluations")
    public ResponseEntity<ApiResponse<List<ClassEvaluationResponseDto>>> getUserEvaluations() {
        Long currentUserId = getCurrentUserId();
        List<ClassEvaluationResponseDto> evaluations = evaluationService.getEvaluationsByUserId(currentUserId);
        return ResponseEntity.ok(new ApiResponse<>(true, "User evaluations retrieved successfully", evaluations));
    }
    
    // Get evaluation by ID
    @GetMapping("/evaluations/{id}")
    public ResponseEntity<ApiResponse<ClassEvaluationResponseDto>> getEvaluationById(@PathVariable Long id) {
        ClassEvaluationResponseDto evaluation = evaluationService.getEvaluationById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Evaluation retrieved successfully", evaluation));
    }
    
    // Update an evaluation
    @PutMapping("/evaluations/{id}")
    public ResponseEntity<ApiResponse<ClassEvaluationResponseDto>> updateEvaluation(
            @PathVariable Long id, @Valid @RequestBody ClassEvaluationRequestDto evaluationRequestDto) {
        ClassEvaluationResponseDto updatedEvaluation = evaluationService.updateEvaluation(id, evaluationRequestDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Evaluation updated successfully", updatedEvaluation));
    }
    
    // Delete an evaluation
    @DeleteMapping("/evaluations/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEvaluation(@PathVariable Long id) {
        evaluationService.deleteEvaluation(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Evaluation deleted successfully", null));
    }
}
