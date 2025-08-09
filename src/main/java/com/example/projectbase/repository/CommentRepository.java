package com.example.projectbase.repository;

import com.example.projectbase.domain.dto.response.comment.CommentResponse;
import com.example.projectbase.domain.entity.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.blog.blogId = :blogId")
    Page<Comment> getAllByBlog_BlogId(Pageable pageable, @Param("blogId") Long blogId);

    @Query("DELETE FROM Comment c WHERE c.blog.blogId = :blogId")
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void deleteAllByBlog_BlogId(@Param("blogId") Long blogId);
}
