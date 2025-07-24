package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Document;
import com.example.projectbase.domain.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("SELECT r FROM Document r WHERE r.title LIKE CONCAT('%', :title, '%')")
    Optional<Document> findByTitle(String title);

    @Query("SELECT u FROM Document u")
    Page<Document> findAll(Pageable pageable);

    @Query("""
                SELECT d
                FROM Document d
                JOIN d.uploader c
                WHERE LOWER(d.title)    LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Document> searchByKeyword(String keyword, Pageable pageable);

    @Query("""
                SELECT d
                FROM Document d
                JOIN d.classRoom c
                WHERE c.classId = :classId
            """)
    Page<Document> searchByClassRoom(@Param("classId") Long classId, Pageable pageable);

}