package com.findme.post.dto.request;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDto {
    @Valid
    private NewPostDto newPostDto;

    @Valid
    private PostImageDto postImageDto;
}
