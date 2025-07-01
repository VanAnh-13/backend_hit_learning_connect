package com.example.projectbase.service;


import com.example.projectbase.domain.dto.response.storage.UploadFileResponseDto;
import com.example.projectbase.security.UserPrincipal;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    UploadFileResponseDto uploadFile(MultipartFile file, UserPrincipal userPrincipal);
    Resource loadFileAsResource(String fileName);
}
