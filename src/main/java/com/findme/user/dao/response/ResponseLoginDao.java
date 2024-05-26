package com.findme.user.dao.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseLoginDao {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("refreshToken")
    private String refreshToken;

    public ResponseLoginDao(Long id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
