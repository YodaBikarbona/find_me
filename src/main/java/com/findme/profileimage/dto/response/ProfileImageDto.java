package com.findme.profileimage.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class ProfileImageDto {

    @NotNull
    @JsonProperty("id")
    private Long id;
    @NotNull
    @JsonProperty("url")
    private String url;

    public ProfileImageDto(Long id, String url) {
        this.id = id;
        this.url = url;
    }

}
