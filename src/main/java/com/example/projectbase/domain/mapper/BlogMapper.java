package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.request.blog.BlogRequest;
import com.example.projectbase.domain.dto.request.blog.BlogUpdateDto;
import com.example.projectbase.domain.dto.request.contest.ContestUpdateDto;
import com.example.projectbase.domain.dto.response.blog.BlogResponse;
import com.example.projectbase.domain.entity.Blog;
import com.example.projectbase.domain.entity.Contest;
import com.example.projectbase.domain.entity.Tag;
import com.example.projectbase.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BlogMapper {

    @Mapping(source = "tags", target = "tags")
    @Mapping(source = "author.fullName", target = "author")
    @Mapping(source = "blogId", target = "blogId")
    BlogResponse toResponse(Blog blog);

    default List<String> mapTagsToNames(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }
        return tags.stream()
                .filter(Objects::nonNull)
                .map(Tag::getName)
                .collect(Collectors.toList());
    }

}
