package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Document;
import com.example.projectbase.domain.entity.User;
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
}
