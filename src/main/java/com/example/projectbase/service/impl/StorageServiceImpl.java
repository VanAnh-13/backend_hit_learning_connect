package com.example.projectbase.service.impl;

import com.example.projectbase.config.StorageProperties;

import com.example.projectbase.domain.dto.response.storage.UploadFileResponseDto;
import com.example.projectbase.exception.extended.FileStorageException;
import com.example.projectbase.exception.extended.NotFoundException;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation;



    @Autowired
    public StorageServiceImpl(StorageProperties storageProperties) {
        this.rootLocation = Paths.get(storageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.rootLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.");
        }
    }

    public UploadFileResponseDto uploadFile(MultipartFile file, UserPrincipal userPrincipal) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence.");
            }

            LocalDate today = LocalDate.now();
            String year  = String.valueOf(today.getYear());
            String month = String.format("%02d", today.getMonthValue());
            String day   = String.format("%02d", today.getDayOfMonth());

            Path dateDir = this.rootLocation
                    .resolve(String.valueOf(userPrincipal.getId()))
                    .resolve(year)
                    .resolve(month)
                    .resolve(day);
            Files.createDirectories(dateDir);

            String ext = "";
            int   idx = fileName.lastIndexOf('.');
            if (idx > 0) {
                ext = fileName.substring(idx);
            }
            String newFilename = UUID.randomUUID().toString() + ext;

            String newPath = String.valueOf(userPrincipal.getId()) + '/' + year + '/' + month + '/' + day + '/' + newFilename;

            Path targetLocation = dateDir.resolve(newFilename);
            Files.copy(
                    file.getInputStream(),
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return UploadFileResponseDto.builder()
                    .fileName(newFilename)
                    .fileSize(file.getSize()+'B')
                    .filePath(newPath.toString())
                    .fileType(file.getContentType())
                    .build();

        } catch (Exception ex) {
            throw new FileStorageException("Could not upload file.");
        }
    }

    public Resource loadFileAsResource(String relativePath) {
        try {
            Path file = rootLocation
                    .resolve(relativePath)
                    .normalize();
            UrlResource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new NotFoundException("Không thể đọc file: " + relativePath);
        } catch (MalformedURLException ex) {
            throw new NotFoundException("File không hợp lệ: " + relativePath);
        }
    }
}
