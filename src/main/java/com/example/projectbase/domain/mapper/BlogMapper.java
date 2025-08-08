package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.request.blog.BlogRequest;
import com.example.projectbase.domain.dto.request.blog.BlogUpdateDto;
import com.example.projectbase.domain.dto.request.contest.ContestUpdateDto;
import com.example.projectbase.domain.dto.response.blog.BlogResponse;
import com.example.projectbase.domain.dto.response.blog.BlogStatiticReponseDto;
import com.example.projectbase.domain.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BlogMapper {

    BlogMapper INSTANCE = Mappers.getMapper(BlogMapper.class);

    @Mapping(source = "author", target = "author", qualifiedByName = "mapUserToString")
    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapTagsToStrings")
    @Mapping(source = "blogId", target = "id")
    @Mapping(source = "imgUrl", target = "imageUrl")
    @Mapping(expression = "java(blog.getComments() != null ? blog.getComments().size() : 0)", target = "commentCount")
    @Mapping(expression = "java(blog.getReactions() != null ? blog.getReactions().size() : 0)", target = "reactionCount")
    BlogResponse toResponse(Blog blog);

    List<BlogResponse> toResponseList(List<Blog> blogs);

    Blog toEntity(BlogRequest request);

    void updateEntity(@MappingTarget Blog blog, BlogUpdateDto request);

    @Mapping(source = "author", target = "author", qualifiedByName = "mapUserToString")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "blogId", target = "id")
    @Mapping(source = "viewCount", target = "viewCount")
    @Mapping(source = "reactions",target = "likeCount",qualifiedByName = "mapReactionsToLikeCount")
    @Mapping(source = "comments",target = "commentCount",qualifiedByName = "mapCommentsToCount")
    BlogStatiticReponseDto toBlogStatiticReponseDto(Blog blog);

    @Named("mapReactionsToLikeCount")
    default long mapReactionsToLikeCount(List<Reaction> reactions){
        return reactions!=null ? reactions.stream().filter(r->r.getType() .equals("LIKE")).count():0;
    }

    @Named("mapCommentsToCount")
    default long mapCommentsToCount(List<Comment> comments){
        return comments != null ? comments.size():0;
    }

    @Named("mapUserToString")
    default String mapUserToString(User user) {
        return user != null ? user.getUsername() : null;
    }

    @Named("mapTagsToStrings")
    default List<String> mapTagsToStrings(List<Tag> tags) {
        return tags == null ? null :
                tags.stream()
                        .map(Tag::getName)
                        .collect(Collectors.toList());
    }
}
