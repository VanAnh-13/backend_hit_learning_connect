package com.example.projectbase.controller;

import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.constant.ResponseMessage;
import com.example.projectbase.domain.dto.request.blog.BlogRequest;
import com.example.projectbase.domain.dto.request.blog.BlogUpdateDto;
import com.example.projectbase.domain.dto.request.comment.CommentRequest;
import com.example.projectbase.domain.dto.request.reaction.ReactionRequest;
import com.example.projectbase.domain.dto.response.blog.BlogResponse;
import com.example.projectbase.domain.dto.response.comment.CommentResponse;
import com.example.projectbase.domain.dto.response.reaction.ReactionReponseDto;
import com.example.projectbase.domain.model.ReactionType;
import com.example.projectbase.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("api/blog")
@RestController
@RequiredArgsConstructor
@Validated

public class BlogController {

    private final BlogService blogService;


    @Tag( name="Blog-USER-controller")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create blog with file image")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BlogResponse> create(
            @RequestPart("request") @Valid BlogRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        BlogResponse response = blogService.create(request, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Tag(name="Blog-USER-controller")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update blog by id ")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlog(@PathVariable Long id, @Valid @RequestBody BlogUpdateDto request){

        try{
            return ResponseEntity.ok(blogService.update(id,request));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Blog.BLOG_NOT_FOUND);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Blog.INTERNAL_SERVER_ERROR);
        }
    }

    @Tag(name="Blog-USER-controller")
    @PreAuthorize("hasRole('USER', 'ADMIN', 'LEADER')")
    @Operation(summary = "Api delete blog by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            blogService.delete(id);
            return VsResponseUtil.success(ResponseMessage.DELETE_SUCCESS);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Blog.BLOG_NOT_FOUND);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Blog.INTERNAL_SERVER_ERROR);
        }
    }

    @Tag(name="Blog-USER-controller")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Api get by ID")
    public ResponseEntity<?> getById(@PathVariable Long id){
      try{
          BlogResponse response=blogService.getById(id);
          return ResponseEntity.ok(response);
      }catch (EntityNotFoundException e){
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Blog.BLOG_NOT_FOUND);
      }catch (Exception e ){
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Blog.INTERNAL_SERVER_ERROR);
      }
    }

    @Tag(name="Blog-USER-controller")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/blog")
    @Operation(summary = "Api get all blog ")
    public ResponseEntity<?> getAllBlog(@ParameterObject Pageable pageable){
        try{
            return ResponseEntity.ok(blogService.getAll(pageable));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Blog.INTERNAL_SERVER_ERROR);
        }
    }

    @Tag(name="Blog-USER-controller")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Api search by Tag")
    @GetMapping("/search")
    public ResponseEntity<?> searchByTag(@RequestParam("tag") String tag, @ParameterObject Pageable pageable){
        try{
            Page<BlogResponse> responses= blogService.searchByTag(tag,pageable);
            return ResponseEntity.ok(responses);
        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Blog.INTERNAL_SERVER_ERROR);
        }
    }

    @Tag(name="Blog-USER-controller")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Api comment ")
    @PostMapping("/comment")
    public ResponseEntity<?> comment(@RequestBody CommentRequest request){
        try{
            CommentResponse response= blogService.comment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Comment.COMMENT_NOT_FOUND);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Comment.INTERNAL_SERVER_ERROR);
        }
    }

    @Tag(name="Blog-USER-controller")
    @PostMapping
    public ResponseEntity<Void> react(@RequestBody ReactionRequest request) {
        blogService.react(request);
        return ResponseEntity.ok().build();
    }

    @Tag(name="Blog-USER-controller")
    @GetMapping("/stats/{blogId}")
    public ResponseEntity<ReactionReponseDto> getStats(@PathVariable Long blogId) {
        return ResponseEntity.ok(blogService.getReactionStats(blogId));
    }

    @Tag(name="Blog-USER-controller")
    @GetMapping("/my-reaction/{blogId}")
    public ResponseEntity<ReactionType> getUserReaction(@PathVariable Long blogId) {
        return blogService.getUserReaction(blogId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
