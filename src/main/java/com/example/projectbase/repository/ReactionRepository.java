package com.example.projectbase.repository;

import com.example.projectbase.domain.entity.Blog;
import com.example.projectbase.domain.entity.Reaction;
import com.example.projectbase.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    Optional<Reaction> findByUserAndBlog(User user, Blog blog);
}
