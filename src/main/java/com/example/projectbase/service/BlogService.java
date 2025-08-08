package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.blog.BlogApprovalRequestDto;
import com.example.projectbase.domain.dto.request.blog.BlogRequest;
import com.example.projectbase.domain.dto.request.blog.BlogUpdateDto;
import com.example.projectbase.domain.dto.request.comment.CommentRequest;
import com.example.projectbase.domain.dto.request.reaction.ReactionRequest;
import com.example.projectbase.domain.dto.response.blog.BlogResponse;
import com.example.projectbase.domain.dto.response.blog.BlogStatiticReponseDto;
import com.example.projectbase.domain.dto.response.comment.CommentResponse;
import com.example.projectbase.domain.dto.response.reaction.ReactionReponseDto;
import com.example.projectbase.domain.model.ReactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface BlogService {

    BlogResponse create(BlogRequest request, MultipartFile file) throws IOException;
    BlogResponse update(Long id, BlogUpdateDto request);
    void delete(Long id);
    BlogResponse getById(Long id);
    Page<BlogResponse> getAll(Pageable pageable);
    Page<BlogResponse> searchByTag(String tag, Pageable pageable);
    CommentResponse comment(CommentRequest request);
    void react(ReactionRequest request);
    ReactionReponseDto getReactionStats(Long blogId);
    Optional<ReactionType> getUserReaction(Long blogId);
    void approveOrRejectBlog(Long blogId, BlogApprovalRequestDto requestDto);
    Page<BlogResponse> getPendingBlogs(Pageable pageable);
    Page<BlogStatiticReponseDto> getBlogStatistics(String fromDate, String toDate, String author, String tag, Pageable pageable);

}
