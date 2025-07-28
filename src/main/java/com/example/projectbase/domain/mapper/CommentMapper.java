package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.request.comment.CommentRequest;
import com.example.projectbase.domain.dto.response.comment.CommentResponse;
import com.example.projectbase.domain.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface CommentMapper {

    CommentResponse toResponse(Comment comment);
    Comment toEntity(CommentRequest request);
}
