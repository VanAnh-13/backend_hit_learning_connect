package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.storage.DeleteFileRequestDto;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
@RestApiV1
@RequiredArgsConstructor
@Slf4j
public class FileStorageController {
    @Autowired
    private StorageService storageService;

    @Tag(name = "storage-controller")
    @Operation(summary = "API để upload tệp đính kèm", description = "Admin / Leader")
    @PostMapping(value = UrlConstant.Storage.UPLOAD_FILE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public ResponseEntity<?> uploadDocument(
            @Parameter(name = "principal", hidden = true)
            @CurrentUser UserPrincipal principal,
            @RequestPart("file") MultipartFile file
    ) {
        return VsResponseUtil.success(storageService.uploadFile(file, principal));
    }


    @Tag(name = "storage-controller")
    @Operation(summary = "API để xóa tệp đính kèm", description = "Admin / Leader")
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    @PostMapping(value = UrlConstant.Storage.DELETE_FILE)
    public ResponseEntity<?> deleteFile(DeleteFileRequestDto deleteFileRequestDto) {
        return VsResponseUtil.success(storageService.deleteFileFromCloudinary(deleteFileRequestDto.getUrl()));
    }
}
