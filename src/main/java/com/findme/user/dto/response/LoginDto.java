package com.findme.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginDto {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("refreshToken")
    private String refreshToken;

    public LoginDto(Long id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
