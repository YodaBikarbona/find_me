package com.findme.post.dto.response;

import com.findme.postimage.dto.response.PostImageDto;

import java.util.List;

public record PostDto(long id, String description, int views, float longitude, float latitude, List<PostImageDto> images) {
}
