package com.example.projectbase.service.impl;

import com.example.projectbase.domain.dto.request.document.DocumentRequestDto;
import com.example.projectbase.domain.dto.response.document.DocumentResponseDto;
import com.example.projectbase.domain.entity.Document;
import com.example.projectbase.domain.mapper.DocumentMapper;
import com.example.projectbase.repository.DocumentRepository;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.DocumentService;
import com.example.projectbase.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final DocumentMapper documentMapper;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, UserRepository userRepository, UserService userService, DocumentMapper documentMapper) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.documentMapper = documentMapper;
    }

    public DocumentResponseDto createDocument(DocumentRequestDto documentRequestDto, UserPrincipal userPrincipal) {
        Document document = Document.builder()
                .title(documentRequestDto.getTitle())
                .description(documentRequestDto.getDescription())
                .fileUrl(documentRequestDto.getFileUrl())
                .uploader(userRepository.findById(userPrincipal.getId()).get())
                .build();

        Document document1 = documentRepository.save(document);

        return DocumentResponseDto.builder()
                .id(String.valueOf(document1.getDocId()))
                .createdAt(document1.getUploadedAt().toString())
                .uploader(userService.getUserById(userPrincipal.getId()))
                .title(document1.getTitle())
                .description(document1.getDescription())
                .fileUrl(document1.getFileUrl())
                .build();
    }

    public DocumentResponseDto getDocumentById(Long id) {
        Document document = documentRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Document not found")
        );

        return documentMapper.toDto(document);
    }

    @Override
    public List<DocumentResponseDto> getAllDocuments(Pageable pageable, String keyword) {
        return documentMapper.toDtoList(documentRepository.searchByKeyword(keyword, pageable).getContent());
    }

    @Override
    public DocumentResponseDto updateDocument(Long id, DocumentRequestDto documentRequestDto) {
        Document document = documentRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Document not found")
        );

        if (!documentRequestDto.getTitle().equals("")) {
            document.setTitle(documentRequestDto.getTitle());
        }

        if (!documentRequestDto.getDescription().equals("")) {
            document.setDescription(documentRequestDto.getDescription());
        }

        if (!documentRequestDto.getFileUrl().equals("")) {
            document.setFileUrl(documentRequestDto.getFileUrl());
        }

        documentRepository.save(document);
        return documentMapper.toDto(document);
    }

    @Override
    public String deleteDocument(Long id) {
        if (documentRepository.findById(id) == null) {
            throw new RuntimeException("Document not found");
        }
        documentRepository.deleteById(id);
        return "Document deleted";
    }


}
