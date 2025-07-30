package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.document.DocumentRequestDto;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
@RestApiV1
@RequiredArgsConstructor
@Slf4j
public class DocumentController {

    @Autowired
    private final DocumentService documentService;

    @Tag(name = "document-ADMIN-controller")
    @Operation(summary = "API create document for an class", description = "Admin / Leader")
    @PostMapping(UrlConstant.Document.CREATE_DOCUMENT)
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public ResponseEntity<?> createDocument(
            @Parameter(name = "principal", hidden = true)
            @CurrentUser UserPrincipal principal,
            @RequestBody DocumentRequestDto documentRequestDto
    ) {
        return VsResponseUtil.success(documentService.createDocument(documentRequestDto, principal));
    }

    @Tags({
            @Tag(name = "document-ADMIN-controller"),
            @Tag(name = "document-USER-controller")
    })
    @Operation(summary = "API get document by documentId", description = "Authenticated")
    @GetMapping(UrlConstant.Document.GET_DOCUMENT)
    public ResponseEntity<?> getDocument(@PathVariable Long documentId) {
        return VsResponseUtil.success(documentService.getDocumentById(documentId));
    }

    @Tags({
            @Tag(name = "document-ADMIN-controller"),
            @Tag(name = "document-USER-controller")
    })
    @Operation(summary = "API get all documents", description = "Authenticated")
    @GetMapping(UrlConstant.Document.BASE)
    public ResponseEntity<?> getAllDocuments(
            @ParameterObject @PageableDefault(page = 0, size = 100, sort = "timestamp", direction = Sort.Direction.ASC)
            Pageable pageable,
            @Parameter(description = "Từ khoá tìm kiếm theo tên tài liệu hoặc người tạo", required = false)
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        return VsResponseUtil.success(documentService.getAllDocuments(pageable, keyword));
    }

    @Tags({
            @Tag(name = "document-ADMIN-controller"),
            @Tag(name = "document-USER-controller")
    })
    @Operation(summary = "API get all documents by classId", description = "Authenticated")
    @GetMapping(UrlConstant.Document.GET_DOCUMENT_BY_CLASSID)
    public ResponseEntity<?> getDocumentByClassId(
            @ParameterObject @PageableDefault(page = 0, size = 100, sort = "docId", direction = Sort.Direction.ASC)
            Pageable pageable,
            @PathVariable Long classId
    ) {
        return VsResponseUtil.success(documentService.getAllDocumentsByClassId(pageable, classId));
    }


    @Tag(name = "document-ADMIN-controller")
    @Operation(summary = "API update document by id", description = "Admin / Leader")
    @PutMapping(UrlConstant.Document.UPDATE_DOCUMENT)
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public ResponseEntity<?> updateDocument(@PathVariable Long documentId, @RequestBody DocumentRequestDto documentRequestDto) {
        return VsResponseUtil.success(documentService.updateDocument(documentId, documentRequestDto));
    }

    @Tag(name = "document-ADMIN-controller")
    @Operation(summary = "API delete document by id", description = "Admin / Leader")
    @DeleteMapping(UrlConstant.Document.DELETE_DOCUMENT)
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public ResponseEntity<?> deleteDocument(@PathVariable Long documentId) {
        return VsResponseUtil.success(documentService.deleteDocument(documentId));
    }

}
