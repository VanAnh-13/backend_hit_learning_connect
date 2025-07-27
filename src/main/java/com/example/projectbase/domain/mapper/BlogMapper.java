package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.request.blog.BlogRequest;
import com.example.projectbase.domain.dto.response.blog.BlogResponse;
import com.example.projectbase.domain.entity.Blog;
import com.example.projectbase.domain.entity.Tag;
import com.example.projectbase.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BlogMapper {

    @Mapping(source = "author", target = "author", qualifiedByName = "mapUserToString")
    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapTagsToStrings")
    @Mapping(source = "blogId", target = "id")
    @Mapping(source = "imgUrl", target = "imageUrl")
    @Mapping(expression = "java(blog.getComments() != null ? blog.getComments().size() : 0)", target = "commentCount")
    @Mapping(expression = "java(blog.getReactions() != null ? blog.getReactions().size() : 0)", target = "reactionCount")
    BlogResponse toResponse(Blog blog);

    List<BlogResponse> toResponseList(List<Blog> blogs);

    Blog toEntity(BlogRequest request);

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
