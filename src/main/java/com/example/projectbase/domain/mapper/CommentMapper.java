package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.request.comment.CommentRequest;
import com.example.projectbase.domain.dto.response.comment.CommentResponse;
import com.example.projectbase.domain.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface CommentMapper {
//
    @Mapping(source = "author.username", target = "username")
    @Mapping(source = "commentId", target = "id")
//    @Mapping(source = "createdAt", target = "createdAt")
    CommentResponse toResponse(Comment comment);
    Comment toEntity(CommentRequest request);
}
