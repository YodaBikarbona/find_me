package com.findme.post.dto.request;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPostDto {
    @Valid
    private NewPostDto newPostDto;

    @Valid
    private NewPostImageDto postImageDto;
}
