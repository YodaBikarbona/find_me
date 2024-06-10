package com.findme.postimage.mapper;

import com.findme.postimage.dto.response.PostImageDto;
import com.findme.postimage.model.PostImageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PostImageMapper {

    public PostImageDto postImageEntityToPostImageDto(PostImageEntity image) {
        return new PostImageDto(image.getId(), image.getUrl(), image.getPost().getId());
    }

}
