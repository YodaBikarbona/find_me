package com.findme.post.mapper;

import com.findme.post.dto.response.PostDto;
import com.findme.post.model.PostEntity;
import com.findme.postimage.dto.response.PostImageDto;
import com.findme.postimage.mapper.PostImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class PostMapper {

    private final PostImageMapper postImageMapper;

    public PostDto postEntityToPostDto(PostEntity post) {
        List<PostImageDto> images = post.getPostImage().stream()
                .map(postImageMapper::postImageEntityToPostImageDto)
                .collect(Collectors.toList());
        return new PostDto(post.getId(), post.getDescription(), post.getViews(), post.getLongitude(), post.getLatitude(), images);
    }

}
