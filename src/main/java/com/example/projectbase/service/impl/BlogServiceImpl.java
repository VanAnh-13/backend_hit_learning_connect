package com.example.projectbase.service.impl;

import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.dto.request.blog.BlogRequest;
import com.example.projectbase.domain.dto.request.blog.BlogUpdateDto;
import com.example.projectbase.domain.dto.request.comment.CommentRequest;
import com.example.projectbase.domain.dto.request.reaction.ReactionRequest;
import com.example.projectbase.domain.dto.response.blog.BlogResponse;
import com.example.projectbase.domain.dto.response.comment.CommentResponse;
import com.example.projectbase.domain.dto.response.reaction.ReactionReponseDto;
import com.example.projectbase.domain.dto.response.storage.UploadFileResponseDto;
import com.example.projectbase.domain.entity.Blog;
import com.example.projectbase.domain.entity.Comment;
import com.example.projectbase.domain.entity.Reaction;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.mapper.BlogMapper;
import com.example.projectbase.domain.mapper.CommentMapper;
import com.example.projectbase.domain.model.ReactionType;
import com.example.projectbase.repository.*;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.BlogService;
import com.example.projectbase.service.StorageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class BlogServiceImpl implements BlogService {

    private static final Logger log = LoggerFactory.getLogger(BlogServiceImpl.class);
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;
    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;
    private final CommentMapper commentMapper;
    private final StorageService storageService;

    @Override
    public BlogResponse create(BlogRequest request, MultipartFile file) throws IOException {
        Blog blog = blogMapper.toEntity(request);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.User.ERR_NOT_FOUND));
        blog.setAuthor(author);

        try {
            if (file == null || file.isEmpty()) {
                throw new RuntimeException(ErrorMessage.Blog.BLOG_NOT_FOUND);
            }

            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isBlank()) {
                throw new RuntimeException(ErrorMessage.Contest.ORIGINAL_FILENAME);
            }

            UploadFileResponseDto responseDto = storageService.uploadFile(
                    file,
                    (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            );

            blog.setImgUrl(responseDto.getFileUrl());

        } catch (RuntimeException e) {
            log.error(ErrorMessage.Contest.VALIDATION_FAILED, e);
            throw e;
        }

        return blogMapper.toResponse(blogRepository.save(blog));
    }


    @Override
    public BlogResponse update(Long id, BlogUpdateDto request) {

        Blog blog= blogRepository.findById(id).orElseThrow(()->new RuntimeException(ErrorMessage.Blog.BLOG_NOT_FOUND));
        blogMapper.updateEntity(blog, request);
        return blogMapper.toResponse(blogRepository.save(blog));
    }

    @Override
    public void delete(Long id) {

        if(!blogRepository.existsById(id)){
            throw new RuntimeException(ErrorMessage.Blog.BLOG_NOT_FOUND);
        }
        blogRepository.deleteById(id);
    }

    @Override
    public BlogResponse getById(Long id) {

        Blog blog= blogRepository.findById(id).orElseThrow(()-> new RuntimeException(ErrorMessage.Blog.BLOG_NOT_FOUND));

        return blogMapper.toResponse(blog);
    }

    @Override
    public Page<BlogResponse> getAll(Pageable pageable) {

        return blogRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public Page<BlogResponse> searchByTag(String tag, Pageable pageable) {
        Page<Blog> blogs = blogRepository.searchByTag(tag, pageable);
        return blogs.map(blogMapper::toResponse);

    }

    @Override
    public CommentResponse comment(CommentRequest request) {

        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        User user= userRepository.findByUsername(username).orElseThrow(()->new RuntimeException(ErrorMessage.User.ERR_NOT_FOUND));

        Blog blog= blogRepository.findById(request.getBlogId()).orElseThrow(()->new RuntimeException(ErrorMessage.Blog.BLOG_NOT_FOUND));

        Comment comment= commentMapper.toEntity(request);
        comment.setAuthor(user);
        comment.setBlog(blog);
        comment.setCreateAt(LocalDateTime.now());

        Comment saveComment= commentRepository.save(comment);


        return commentMapper.toResponse(saveComment);
    }

    @Override
    public void react(ReactionRequest request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.User.ERR_NOT_FOUND));

        Blog blog = blogRepository.findById(request.getBlogId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.Blog.BLOG_NOT_FOUND));

        Optional<Reaction> existingOpt = reactionRepository.findByUserAndBlog(user, blog);

        if (existingOpt.isPresent()) {
            Reaction existing = existingOpt.get();
            if (existing.getType() == request.getType()) {
                reactionRepository.delete(existing);
            } else {
                existing.setType(request.getType());
//                existing.setReactedAt(LocalDateTime.now());
                reactionRepository.save(existing);
            }
        } else {
            Reaction reaction = new Reaction();
            reaction.setUser(user);
            reaction.setBlog(blog);
            reaction.setType(request.getType());
//            reaction.setReactedAt(LocalDateTime.now());
            reactionRepository.save(reaction);
        }
    }

    @Override
    public ReactionReponseDto getReactionStats(Long blogId) {
        List<Object[]> result = reactionRepository.countReactionsByType(blogId);
        Map<ReactionType, Long> map = new EnumMap<>(ReactionType.class);
        for (Object[] row : result) {
            ReactionType type = (ReactionType) row[0];
            Long count = (Long) row[1];
            map.put(type, count);
        }
        return new ReactionReponseDto(map);
    }

    @Override
    public Optional<ReactionType> getUserReaction(Long blogId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return reactionRepository.findByUser_UsernameAndBlog_BlogId(username, blogId)
                .map(Reaction::getType);
    }

    public BlogResponse toResponse(Blog blog){

        return BlogResponse.builder()
                .id(blog.getBlogId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .createdAt(blog.getCreatedAt())
                .updatedAt(blog.getUpdatedAt())
                .imageUrl(blog.getImgUrl())
                .build();
    }
}
