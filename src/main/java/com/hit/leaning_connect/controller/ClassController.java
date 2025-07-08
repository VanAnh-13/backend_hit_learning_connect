package com.hit.leaning_connect.controller;

import com.hit.leaning_connect.domain.reponse.ApiResponse;
import com.hit.leaning_connect.domain.reponse.ClassResponseDto;
import com.hit.leaning_connect.domain.request.ClassRequestDto;
import com.hit.leaning_connect.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/classes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
public class ClassController {

    private final ClassService classService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClassResponseDto>>> getAllClasses() {
        List<ClassResponseDto> classes = classService.getALlClass();
        return ResponseEntity.ok(new ApiResponse<>(true, "Classes retrieved successfully", classes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassResponseDto>> getClassById(@PathVariable Long id) {
        ClassResponseDto classDto = classService.getClassById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Class retrieved successfully", classDto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClassResponseDto>> createClass(@RequestBody ClassRequestDto classDto) {
        ClassResponseDto createdClass = classService.addNewClass(classDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Class created successfully", createdClass));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClassResponseDto>> updateClass(@PathVariable Long id, @RequestBody ClassRequestDto classDto) {
        ClassResponseDto updatedClass = classService.editClass(id, classDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Class updated successfully", updatedClass));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClass(@PathVariable Long id) {
        classService.deleteClass(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Class deleted successfully", null));
    }
}