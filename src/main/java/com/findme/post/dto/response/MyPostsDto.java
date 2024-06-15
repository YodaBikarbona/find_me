package com.findme.post.dto.response;

import com.findme.postimage.dto.response.PostImageDto;

import java.util.List;

public record MyPostsDto(long id, List<PostImageDto> images) {
}
