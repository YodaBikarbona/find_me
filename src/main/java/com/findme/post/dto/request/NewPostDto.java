package com.findme.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPostDto {
    @NotNull
    @NotBlank
    private String description;

    @NotNull
    private float longitude;

    @NotNull
    private float latitude;
}
