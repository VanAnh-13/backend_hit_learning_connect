package com.example.projectbase.repository;

import com.example.projectbase.domain.dto.response.comment.CommentResponse;
import com.example.projectbase.domain.entity.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.blog.blogId = :blogId")
    Page<Comment> getAllByBlog_BlogId(Pageable pageable, @Param("blogId") Long blogId);
}
