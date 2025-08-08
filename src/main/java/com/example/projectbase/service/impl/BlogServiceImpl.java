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
import com.example.projectbase.domain.entity.*;
import com.example.projectbase.domain.mapper.BlogMapper;
import com.example.projectbase.domain.mapper.CommentMapper;
import com.example.projectbase.domain.model.ReactionType;
import com.example.projectbase.exception.extended.NotFoundException;
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
import java.util.*;

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
    public BlogResponse create(BlogRequest request, UserPrincipal user) throws IOException {
        String tagString = request.getTags().trim();

        String[] parts = tagString.split("\\s+");
        ;

        String[] Stags = Arrays.stream(parts)
                .map(s -> s.startsWith("#") ? s.substring(1) : s)
                .toArray(String[]::new);

        List<Tag> tags = new ArrayList<>();

        for (int i = 0; i < Stags.length; i++) {
            Tag tag = tagRepository.findByName(Stags[i]);
            if (tag == null) {
                tags.add(tagRepository.save(Tag.builder().name(Stags[i]).build()));
            } else {
                tags.add(tag);
            }

        }

        Blog blog = Blog.builder()
                .author(userRepository.findById(user.getId()).get())
                .title(request.getTitle())
                .urlFile(request.getUrlFile())
                .description(request.getDescription())
                .tags(tags)
                .build();

        for (Tag tag : blog.getTags()) {
            System.out.println(tag.getName());
        }

        return blogMapper.toResponse(blogRepository.save(blog));
    }


    @Override
    public BlogResponse update(Long id, BlogUpdateDto request) {

        Blog blog = blogRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.Blog.BLOG_NOT_FOUND));

        String tagString = request.getTags().trim();

        String[] parts = tagString.split("\\s+");
        ;

        String[] Stags = Arrays.stream(parts)
                .map(s -> s.startsWith("#") ? s.substring(1) : s)
                .toArray(String[]::new);

        List<Tag> tags = new ArrayList<>();

        for (int i = 0; i < Stags.length; i++) {
            Tag tag = tagRepository.findByName(Stags[i]);
            if (tag == null) {
                tags.add(tagRepository.save(Tag.builder().name(Stags[i]).build()));
            } else {
                tags.add(tag);
            }

        }

        blog.getTags().clear();
        blog.setTags(tags);

        blog.setTitle(request.getTitle());
        blog.setUrlFile(request.getUrlFile());
        blog.setDescription(request.getDescription());
        blog.setUpdatedAt(LocalDateTime.now());

        return blogMapper.toResponse(blogRepository.save(blog));
    }

    @Override
    public void delete(Long id) {

        if (!blogRepository.existsById(id)) {
            throw new RuntimeException(ErrorMessage.Blog.BLOG_NOT_FOUND);
        }
        blogRepository.deleteById(id);
    }

    @Override
    public BlogResponse getById(Long id) {

        Blog blog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException(ErrorMessage.Blog.BLOG_NOT_FOUND));

        return blogMapper.toResponse(blog);
    }

    @Override
    public Page<BlogResponse> getAll(Pageable pageable) {

        return blogRepository.findAll(pageable).map(blogMapper::toResponse);
    }

    @Override
    public Page<BlogResponse> searchByTag(String tag, Pageable pageable) {
        Page<Blog> blogs = blogRepository.searchByTag(tag, pageable);
        return blogs.map(blogMapper::toResponse);

    }

    @Override
    public CommentResponse comment(CommentRequest request, UserPrincipal userPrincipal) throws IOException {

        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new RuntimeException(ErrorMessage.User.ERR_NOT_FOUND));

        Blog blog = blogRepository.findById(request.getBlogId()).orElseThrow(() -> new RuntimeException(ErrorMessage.Blog.BLOG_NOT_FOUND));

        Comment comment = commentMapper.toEntity(request);
        comment.setAuthor(user);
        comment.setBlog(blog);
        comment.setCreatedAt(LocalDateTime.now());

        Comment saveComment = commentRepository.save(comment);


        return commentMapper.toResponse(saveComment);
    }

    @Override
    public Page<CommentResponse> getAllCommentsByBlogId(Pageable pageable, Long blogId) {
        return commentRepository.getAllByBlog_BlogId(pageable, blogId)
                .map(commentMapper::toResponse);
    }

    @Override
    public void deleteComment(UserPrincipal user, Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Comment.COMMENT_NOT_FOUND)
        );

        Blog blog = comment.getBlog();

        User author = blog.getAuthor();

        User writer = comment.getAuthor();

        if (author.getId().equals(user.getId()) || writer.getId().equals(user.getId())) {
            commentRepository.delete(comment);
        }

    }

    @Override
    public CommentResponse getComment(Long commentId) {
        return commentMapper.toResponse(commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Comment.COMMENT_NOT_FOUND)
        ));
    }

    @Override
    public CommentResponse updateComment(UserPrincipal user, Long commentId, String content) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Comment.COMMENT_NOT_FOUND)
        );
        comment.setContent(content);
        if (comment.getAuthor().getId().equals(user.getId())) {
            commentRepository.save(comment);
        } else {
            throw new NotFoundException(ErrorMessage.Comment.COMMENT_NOT_FOUND);
        }

        return commentMapper.toResponse(comment);
    }

    @Override
    public void react(ReactionRequest request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
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

    public BlogResponse toResponse(Blog blog) {

        return BlogResponse.builder()
                .title(blog.getTitle())
//                .content(blog.getContent())
                .createdAt(blog.getCreatedAt())
                .updatedAt(blog.getUpdatedAt())
//                .imageUrl(blog.getImgUrl())
                .build();
    }
}
