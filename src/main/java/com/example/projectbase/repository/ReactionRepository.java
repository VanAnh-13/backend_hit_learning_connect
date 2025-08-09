package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Blog;
import com.example.projectbase.domain.entity.Reaction;
import com.example.projectbase.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    Optional<Reaction> findByUserAndBlog(User user, Blog blog);

    @Query("SELECT r.type, COUNT(r) FROM Reaction r WHERE r.blog.blogId = :blogId GROUP BY r.type")
    List<Object[]> countReactionsByType(@Param("blogId") Long blogId);

    Optional<Reaction> findByUser_UsernameAndBlog_BlogId(String username, Long blogId);

    @Query("DELETE FROM Reaction c WHERE c.blog.blogId = :blogId")
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void deleteAllByBlog_BlogId(@io.lettuce.core.dynamic.annotation.Param("blogId") Long blogId);
}
