package com.hit.leaning_connect.domain.mapper;

import com.hit.leaning_connect.domain.entity.Document;
import com.hit.leaning_connect.domain.reponse.DocumentResponseDto;
import com.hit.leaning_connect.domain.request.DocumentRequestDto;
import org.springframework.stereotype.Component;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    @Mapping(target = "docId", ignore = true)
    @Mapping(target = "uploader", ignore = true)
    @Mapping(target = "uploadedAt", ignore = true)
    Document toEntity(DocumentRequestDto dto);

    @Mapping(source = "docId", target = "id")
    @Mapping(source = "uploader.userId", target = "uploaderId")
    @Mapping(source = "uploader.username", target = "uploaderUsername")
    @Mapping(source = "uploader.fullName", target = "uploaderFullName")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "fileUrl", target = "fileUrl")
    @Mapping(source = "uploadedAt", target = "uploadedAt")
    DocumentResponseDto toDTO(Document document);
}
