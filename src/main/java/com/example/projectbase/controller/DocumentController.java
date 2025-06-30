package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.document.DocumentRequestDto;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.DocumentService;
import com.example.projectbase.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;

import static com.mysql.cj.conf.PropertyKey.logger;

@RestController
@Validated
@RestApiV1
@RequiredArgsConstructor
@Slf4j
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    private final DocumentService documentService;

    @Tag(name = "document-controller")
    @Operation(summary = "API create document")
    @PostMapping(UrlConstant.Document.CREATE_DOCUMENT)
    public ResponseEntity<?> createDocument(
            @Parameter(name = "principal", hidden = true)
            @CurrentUser UserPrincipal principal,
            DocumentRequestDto documentRequestDto
    ) {
        return VsResponseUtil.success(documentService.createDocument(documentRequestDto, principal));
    }

    @Tag(name = "document-controller")
    @Operation(summary = "API get document by documentId")
    @GetMapping(UrlConstant.Document.GET_DOCUMENT)
    public ResponseEntity<?> getDocument(@PathVariable Long documentId) {
        return VsResponseUtil.success(documentService.getDocumentById(documentId));
    }

    @Tag(name = "document-controller")
    @Operation(summary = "API get all documents")
    @GetMapping(UrlConstant.Document.BASE)
    public ResponseEntity<?> getAllDocuments(@ParameterObject @PageableDefault(page = 0, size = 100, sort = "timestamp", direction = Sort.Direction.ASC)
                                                 Pageable pageable) {
        return VsResponseUtil.success(documentService.getAllDocuments(pageable));
    }

    @Tag(name = "document-controller")
    @Operation(summary = "API update document by id")
    @PutMapping(UrlConstant.Document.UPDATE_DOCUMENT)
    public ResponseEntity<?> updateDocument(@PathVariable Long documentId, @ParameterObject DocumentRequestDto documentRequestDto) {
        return VsResponseUtil.success(documentService.updateDocument(documentId, documentRequestDto));
    }

    @Tag(name = "document-controller")
    @Operation(summary = "API delete document by id")
    @DeleteMapping(UrlConstant.Document.DELETE_DOCUMENT)
    public ResponseEntity<?> deleteDocument(@PathVariable Long documentId) {
        return VsResponseUtil.success(documentService.deleteDocument(documentId));
    }

}
