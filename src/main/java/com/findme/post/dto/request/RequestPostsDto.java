package com.findme.post.dto.request;

public record RequestPostsDto(float longitude, float latitude, Integer radius, int limit) {
}
