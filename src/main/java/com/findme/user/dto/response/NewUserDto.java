package com.findme.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewUserDto {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("username")
    private String username;
    @JsonProperty("phoneNumber")
    private String phoneNumber;

    public NewUserDto(Long id, String email, String username, String phoneNumber) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
    }
}
