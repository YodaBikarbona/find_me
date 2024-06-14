package com.findme.post.dto.response;

import com.findme.postcomment.dto.response.PostCommentDto;
import com.findme.postimage.dto.response.PostImageDto;

import java.util.List;

public record PostDto(
        long id,
        String description,
        int views,
        float longitude,
        float latitude,
        List<PostImageDto> images,
        List<PostCommentDto> comments,
        PostProfileDto profile) {
}
