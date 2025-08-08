package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    Page<Blog> findByTags_NameContainingIgnoreCase(String tagName, Pageable pageable);

    List<Blog> findAllByAuthor_Username(String username);

    @Query("SELECT b FROM Blog b JOIN b.tags t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :tag, '%'))")
    Page<Blog> searchByTag(@Param("tag") String tag, Pageable pageable);

    Page<Blog> findByStatus(String status, Pageable pageable);

    @Query("""
        SELECT b FROM Blog b
        WHERE (:fromDate IS NULL OR b.createdAt >= :fromDate)
          AND (:toDate IS NULL OR b.createdAt <= :toDate)
          AND (:author IS NULL OR b.author.username LIKE %:author%)
          AND (:tag IS NULL OR EXISTS (
                SELECT t FROM b.tags t
                WHERE t.name LIKE %:tag%
              ))
        """)
    Page<Blog> findStatistics(
            LocalDate fromDate,
            LocalDate toDate,
            String author,
            String tag,
            Pageable pageable
    );

}
