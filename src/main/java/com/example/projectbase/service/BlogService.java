package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.blog.BlogRequest;
import com.example.projectbase.domain.dto.request.blog.BlogUpdateDto;
import com.example.projectbase.domain.dto.request.comment.CommentRequest;
import com.example.projectbase.domain.dto.request.reaction.ReactionRequest;
import com.example.projectbase.domain.dto.response.blog.BlogResponse;
import com.example.projectbase.domain.dto.response.comment.CommentResponse;
import com.example.projectbase.domain.dto.response.reaction.ReactionReponseDto;
import com.example.projectbase.domain.model.ReactionType;
import com.example.projectbase.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface BlogService {

    BlogResponse create(BlogRequest request, UserPrincipal user) throws IOException;
    BlogResponse update(Long id, BlogUpdateDto request, UserPrincipal user);
    void delete(Long id, UserPrincipal user);
    BlogResponse getById(Long id);
    Page<BlogResponse> getAll(Pageable pageable);
    Page<BlogResponse> searchByTag(String tag, Pageable pageable);
    CommentResponse comment(CommentRequest request, UserPrincipal user) throws IOException;
    void react(ReactionRequest request);
    ReactionReponseDto getReactionStats(Long blogId);
    Optional<ReactionType> getUserReaction(Long blogId);
    Page<CommentResponse> getAllCommentsByBlogId(Pageable pageable, Long blogId);

    void deleteComment(UserPrincipal user, Long commentId);

    CommentResponse getComment(Long commentId);

    CommentResponse updateComment(UserPrincipal user, Long commentId, String content);
}
