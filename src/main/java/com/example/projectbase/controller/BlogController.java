package com.example.projectbase.controller;

import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.constant.ResponseMessage;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.blog.BlogRequest;
import com.example.projectbase.domain.dto.request.blog.BlogUpdateDto;
import com.example.projectbase.domain.dto.request.blog.SearchBlogRequest;
import com.example.projectbase.domain.dto.request.comment.CommentRequest;
import com.example.projectbase.domain.dto.request.reaction.ReactionRequest;
import com.example.projectbase.domain.dto.response.blog.BlogResponse;
import com.example.projectbase.domain.dto.response.comment.CommentResponse;
import com.example.projectbase.domain.dto.response.reaction.ReactionReponseDto;
import com.example.projectbase.domain.model.ReactionType;
import com.example.projectbase.security.CurrentUser;
import com.example.projectbase.security.UserPrincipal;
import com.example.projectbase.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Validated
@RestApiV1
public class BlogController {

    private final BlogService blogService;

    @Operation(summary = "Api create blog with url file")
    @PostMapping(UrlConstant.Blog.CREATE_BLOG)
    public ResponseEntity<?> create(@RequestBody @Valid BlogRequest request, @Parameter(name = "principal", hidden = true) @CurrentUser UserPrincipal user) throws IOException {
        return VsResponseUtil.success(HttpStatus.CREATED, blogService.create(request, user));
    }


    @Operation(summary = "Api update blog by id ")
    @PutMapping(UrlConstant.Blog.UPDATE_BLOG)
    public ResponseEntity<?> updateBlog(@PathVariable Long blogId, @Valid @RequestBody BlogUpdateDto request, @Parameter(name = "principal", hidden = true) @CurrentUser UserPrincipal user){
            return VsResponseUtil.success(HttpStatus.OK, blogService.update(blogId, request, user));
    }

    @Operation(summary = "Api delete blog by id")
    @DeleteMapping(UrlConstant.Blog.DELETE_BLOG)
    public ResponseEntity<?> delete(@PathVariable Long blogId, @Parameter(name = "principal", hidden = true) @CurrentUser UserPrincipal user){
            blogService.delete(blogId, user);
            return VsResponseUtil.success(ResponseMessage.DELETE_SUCCESS);
    }

    @GetMapping(UrlConstant.Blog.GET_BLOG)
    @Operation(summary = "Api get blog by ID")
    public ResponseEntity<?> getById(@PathVariable Long blogId){
          return VsResponseUtil.success(HttpStatus.OK ,blogService.getById(blogId));
    }

    @GetMapping(UrlConstant.Blog.BASE)
    @Operation(summary = "Api get all blogs")
    public ResponseEntity<?> getAllBlog(@ParameterObject @PageableDefault(page = 0, size = 1000, sort = "blogId", direction = Sort.Direction.ASC) Pageable pageable){

            return VsResponseUtil.success(HttpStatus.OK, blogService.getAll(pageable));
    }

    @PostMapping(UrlConstant.Blog.SEARCH)
    @Operation(summary = "Api get all blogs by keyword")
    public ResponseEntity<?> findBlog(@RequestBody SearchBlogRequest search, @ParameterObject @PageableDefault(page = 0, size = 1000, sort = "blogId", direction = Sort.Direction.ASC) Pageable pageable){

        return VsResponseUtil.success(HttpStatus.OK, blogService.findBlog(search ,pageable));
    }

    //--------------------------------tag--------------------------------------

    @Operation(summary = "Api search by Tag")
    @GetMapping(UrlConstant.Blog.SEARCH_BLOG)
    public ResponseEntity<?> searchByTag(@PathVariable String tag, @ParameterObject @PageableDefault(page = 0, size = 1000, sort = "blogId", direction = Sort.Direction.ASC) Pageable pageable){
        return VsResponseUtil.success(HttpStatus.OK, blogService.searchByTag(tag,pageable));
    }

    // ------------------------------------- Comment api --------------------------------------
    @Operation(summary = "Api comment to blog")
    @PostMapping(UrlConstant.Blog.COMMENT_BLOG)
    public ResponseEntity<?> comment(@RequestBody CommentRequest request, @Parameter(name = "principal", hidden = true) @CurrentUser UserPrincipal user){
        try{
            CommentResponse response= blogService.comment(request, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.Comment.COMMENT_NOT_FOUND);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.Comment.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Api get comment by id")
    @GetMapping(UrlConstant.Blog.GET_COMMENT)
    public ResponseEntity<?> getComment(@PathVariable Long commentId){
        return ResponseEntity.ok(blogService.getComment(commentId));
    }

    @Operation(summary = "Api get all comments")
    @GetMapping(UrlConstant.Blog.GET_ALL_COMMENTS)
    public ResponseEntity<?> getAllComments(@PathVariable Long blogId ,@ParameterObject @PageableDefault(page = 0, size = 1000, sort = "commentId", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(blogService.getAllCommentsByBlogId(pageable ,blogId));
    }

    @Operation(summary = "Api delete Comment by Blog Author and Comment Owner")
    @DeleteMapping(UrlConstant.Blog.DELETE_COMMENTS)
    public ResponseEntity<?> deleteComments(@Parameter(name = "principal", hidden = true) @CurrentUser UserPrincipal user, @PathVariable Long commentId) {
        blogService.deleteComment(user, commentId);
        return ResponseEntity.ok("Delete Success");
    }

    @Operation(summary = "Api update Comment by Comment Owner")
    @PutMapping(UrlConstant.Blog.UPDATE_COMMENT)
    public ResponseEntity<?>  updateComments(@Parameter(name = "principal", hidden = true) @CurrentUser UserPrincipal user, @PathVariable Long commentId, @RequestBody String content){
        return ResponseEntity.ok(blogService.updateComment(user, commentId, content));
    }


    // ------------------------------------- React api --------------------------------------
    @Operation(summary = "Api drop a react to a blog id or comment id by type in request")
    @PostMapping(UrlConstant.Blog.DROP_REACT)
    public ResponseEntity<?> dropReact(@RequestBody ReactionRequest request) {
        blogService.react(request);
        return ResponseEntity.ok("Drop react to blog successful");
    }

    @Operation(summary = "Api get react by id comment or blog")
    @GetMapping(UrlConstant.Blog.GET_REACT)
    public ResponseEntity<?> getReact(@RequestBody ReactionRequest request) {
        blogService.react(request);
        return ResponseEntity.ok("get react to blog successful");
    }

}
