package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.response.document.DocumentResponseDto;
import com.example.projectbase.domain.entity.Document;

import com.nimbusds.openid.connect.sdk.claims.Gender;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    @Mapping(source = "docId", target = "id")
    @Mapping(source = "uploadedAt", target = "createdAt")
    DocumentResponseDto toDto(Document document);

    List<DocumentResponseDto> toDtoList(List<Document> documents);
}
