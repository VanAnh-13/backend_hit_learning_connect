package com.hit.leaning_connect.service;

import com.hit.leaning_connect.domain.reponse.DocumentResponseDto;
import com.hit.leaning_connect.domain.request.DocumentRequestDto;

import java.util.List;

public interface DocumentService {
    List<DocumentResponseDto> getAllDocuments();

    DocumentResponseDto getDocumentById(Long id);

    DocumentResponseDto addDocument(DocumentRequestDto documentRequestDto, Long uploaderId);

    DocumentResponseDto updateDocument(Long id, DocumentRequestDto documentRequestDto);

    void deleteDocument(Long id);
}
