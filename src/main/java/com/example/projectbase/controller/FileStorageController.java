package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;

@RestController
@Validated
@RestApiV1
@RequiredArgsConstructor
@Slf4j
public class FileStorageController {
    @Autowired
    private StorageService storageService;

    @Tag(name = "storage-controller")
    @Operation(summary = "API để upload tệp đính kèm")
    @PostMapping(value = UrlConstant.Storage.UPLOAD_FILE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadDocument(
            @Parameter(name = "principal", hidden = true)
            @CurrentUser UserPrincipal principal,
            @RequestPart("file") MultipartFile file
    ) {
        return VsResponseUtil.success(storageService.uploadFile(file, principal));
    }

    @Tag(name = "storage-controller")
    @Operation(summary = "API để download tệp đính kèm")
    @GetMapping(UrlConstant.Storage.DOWNLOAD_FILE)
    public ResponseEntity<?> downloadDocument(HttpServletRequest request) {
        String pathWithinApp = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestPattern = (String) request.getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        String relativePath = new AntPathMatcher()
                .extractPathWithinPattern(bestPattern, pathWithinApp);

        Resource resource = storageService.loadFileAsResource(relativePath);

        String contentType;
        try {
            contentType = request.getServletContext()
                    .getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
