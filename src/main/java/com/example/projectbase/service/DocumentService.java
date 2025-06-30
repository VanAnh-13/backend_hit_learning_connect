package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.document.DocumentRequestDto;
import com.example.projectbase.domain.dto.response.document.DocumentResponseDto;
import com.example.projectbase.security.UserPrincipal;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DocumentService {
    DocumentResponseDto createDocument(DocumentRequestDto documentRequestDto, UserPrincipal userPrincipal);
    DocumentResponseDto getDocumentById(Long id);
    List<DocumentResponseDto> getAllDocuments(Pageable pageable);
    DocumentResponseDto updateDocument(Long id, DocumentRequestDto documentRequestDto);
    String deleteDocument(Long id);
}
