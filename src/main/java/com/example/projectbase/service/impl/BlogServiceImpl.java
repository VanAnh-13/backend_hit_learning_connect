package com.example.projectbase.service.impl;

import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.domain.dto.request.blog.BlogRequest;
import com.example.projectbase.domain.dto.request.blog.BlogUpdateDto;
import com.example.projectbase.domain.dto.request.comment.CommentRequest;
import com.example.projectbase.domain.dto.request.reaction.ReactionRequest;
import com.example.projectbase.domain.dto.response.blog.BlogResponse;
import com.example.projectbase.domain.dto.response.comment.CommentResponse;
import com.example.projectbase.domain.entity.Blog;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.domain.mapper.BlogMapper;
import com.example.projectbase.repository.*;
import com.example.projectbase.service.BlogService;
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

    @Override
    public BlogResponse create(BlogRequest request, MultipartFile file) throws IOException {

        Blog blog= blogMapper.toEntity(request);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.User.ERR_NOT_FOUND));
        blog.setAuthor(author);

        try {
            if (file == null || file.isEmpty()) {
                throw new RuntimeException(ErrorMessage.Blog.BLOG_NOT_FOUND);
            }

            String originalFileName = file.getOriginalFilename();

            if (file!= null && !file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", "blog");
                Files.createDirectories(uploadPath);
                Path filePath = uploadPath.resolve(fileName);
                file.transferTo(filePath.toFile());
                blog.setImgUrl(filePath.toString());
            }
        }catch (IOException e) {
            log.error(ErrorMessage.Contest.UPLOADING_FILE, e);
            throw new RuntimeException(ErrorMessage.Contest.FILE_UPLOAD_FAILED, e);
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
        return null;
    }

    @Override
    public CommentResponse comment(CommentRequest request) {
        return null;
    }

    @Override
    public void react(ReactionRequest request) {

    }

    public BlogResponse toResponse(Blog blog){

        return BlogResponse.builder()
                .id(blog.getBlogId())
                .title(blog.getTitle())
                .
    }
}
