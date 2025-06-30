package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.response.document.DocumentResponseDto;
import com.example.projectbase.domain.entity.Document;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    @Mapping(source = "docId", target = "id")
    @Mapping(source = "uploadedAt", target = "createdAt")
    @Mapping(source = "uploader", target = "creator")
    DocumentResponseDto toDto(Document document);

    List<DocumentResponseDto> toDtoList(List<Document> documents);
}
