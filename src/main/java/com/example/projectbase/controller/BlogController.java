package com.example.projectbase.controller;

import com.example.projectbase.domain.dto.request.blog.BlogRequest;
import com.example.projectbase.domain.dto.response.blog.BlogResponse;
import com.example.projectbase.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("api/blog")
@RestController
@RequiredArgsConstructor
@Validated

public class BlogController {

    private final BlogService blogService;

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create blog with file image")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BlogResponse> create(
            @RequestPart("request") @Valid BlogRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        BlogResponse response = blogService.create(request, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }





}
