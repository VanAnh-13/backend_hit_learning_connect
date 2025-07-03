package com.example.projectbase.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.projectbase.config.StorageProperties;

import com.example.projectbase.domain.dto.response.storage.UploadFileResponseDto;
import com.example.projectbase.exception.extended.FileStorageException;
import com.example.projectbase.exception.extended.NotFoundException;
import com.example.projectbase.exception.extended.UploadFileException;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.StorageService;
import com.example.projectbase.util.UploadFileUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional

public class StorageServiceImpl implements StorageService {

    private final Path rootLocation;

    private final Cloudinary cloudinary;

    private final Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Autowired
    public StorageServiceImpl(StorageProperties storageProperties, Cloudinary cloudinary) {
        this.rootLocation = Paths.get(storageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.cloudinary = cloudinary;

        try {
            Files.createDirectories(this.rootLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.");
        }
    }

    public UploadFileResponseDto uploadFile(MultipartFile file, UserPrincipal userPrincipal) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (originalFilename.contains("..")) {
                throw new FileStorageException("Invalid path sequence in filename.");
            }

            LocalDate today = LocalDate.now();
            String folder = String.format("%s/%d/%02d/%02d",
                    userPrincipal.getId(),
                    today.getYear(),
                    today.getMonthValue(),
                    today.getDayOfMonth());

            String publicId = folder + "/" + originalFilename;

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "raw",
                            "public_id", publicId,
                            "overwrite", true
                    )
            );

            String downloadUrl = (String) uploadResult.get("secure_url");
            long size = ((Number) uploadResult.get("bytes")).longValue();

            return UploadFileResponseDto.builder()
                    .fileName(originalFilename)
                    .filePath(downloadUrl)
                    .fileSize(size)
                    .fileType(StringUtils.getFilenameExtension(originalFilename))
                    .build();

        } catch (IOException e) {
            throw new FileStorageException("Could not upload file to Cloudinary.");
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        return null;
    }

    public String deleteFileFromCloudinary(String url) {
        try {
            String publicId = extractPublicIdFromUrl(url);

            Map<?, ?> deleteResult = cloudinary.uploader().destroy(publicId,
                    ObjectUtils.asMap("resource_type", "raw"));

            String result = (String) deleteResult.get("result");
            if (!"ok".equals(result)) {
                throw new FileStorageException("Failed to delete file from Cloudinary: " + publicId);
            }
        } catch (IOException e) {
            throw new FileStorageException("Could not delete file from Cloudinary.");
        }

        return "Delete Success";
    }

    public String extractPublicIdFromUrl(String url) {
        try {
            String[] parts = url.split("/upload/");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Invalid Cloudinary URL");
            }

            String pathWithVersion = parts[1];

            String path = pathWithVersion.replaceFirst("v\\d+/", "");

            return URLDecoder.decode(path, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to extract public_id from URL", e);
        }
    }

}
