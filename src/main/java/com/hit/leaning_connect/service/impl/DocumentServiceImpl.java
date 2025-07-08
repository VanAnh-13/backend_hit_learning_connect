package com.hit.leaning_connect.service.impl;

import com.hit.leaning_connect.domain.entity.Document;
import com.hit.leaning_connect.domain.entity.User;
import com.hit.leaning_connect.domain.mapper.DocumentMapper;
import com.hit.leaning_connect.domain.reponse.DocumentResponseDto;
import com.hit.leaning_connect.domain.request.DocumentRequestDto;
import com.hit.leaning_connect.exception.ResourceNotFoundException;
import com.hit.leaning_connect.repository.DocumentRepository;
import com.hit.leaning_connect.repository.UserRepository;
import com.hit.leaning_connect.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final DocumentMapper documentMapper;

    @Override
    public List<DocumentResponseDto> getAllDocuments() {
        return documentRepository.findAll().stream()
                .map(documentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentResponseDto getDocumentById(Long id) {
        return documentRepository.findById(id)
                .map(documentMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Document", "id", id));
    }

    @Override
    public DocumentResponseDto addDocument(DocumentRequestDto documentRequestDto, Long uploaderId) {
        User uploader = userRepository.findById(uploaderId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", uploaderId));
        
        Document document = documentMapper.toEntity(documentRequestDto);
        document.setUploader(uploader);
        
        Document savedDocument = documentRepository.save(document);
        return documentMapper.toDTO(savedDocument);
    }

    @Override
    public DocumentResponseDto updateDocument(Long id, DocumentRequestDto documentRequestDto) {
        Document existingDocument = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document", "id", id));
        
        existingDocument.setTitle(documentRequestDto.title());
        existingDocument.setDescription(documentRequestDto.description());
        existingDocument.setFileUrl(documentRequestDto.fileUrl());
        
        Document updatedDocument = documentRepository.save(existingDocument);
        return documentMapper.toDTO(updatedDocument);
    }

    @Override
    public void deleteDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Document", "id", id);
        }
        documentRepository.deleteById(id);
    }
}
