package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    Page<Blog> findByTags_NameContainingIgnoreCase(String tagName, Pageable pageable);

    List<Blog> findAllByAuthor_Username(String username);

    @Query("SELECT b FROM Blog b JOIN b.tags t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :tag, '%'))")
    Page<Blog> searchByTag(@Param("tag") String tag, Pageable pageable);

    @Query("SELECT b FROM Blog b WHERE b.title LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Blog> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
