package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    Page<Blog> findByTags_NameContainingIgnoreCase(String tagName, Pageable pageable);

    List<Blog> findAllByAuthor_Username(String username);


}
