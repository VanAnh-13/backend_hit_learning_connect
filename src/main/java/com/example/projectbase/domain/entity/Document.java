package com.example.projectbase.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id")
    private Long docId;

    @ManyToOne
    @JoinColumn(name = "uploader_id")
    private User uploader;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassRoom classRoom;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @CreationTimestamp
    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();
}