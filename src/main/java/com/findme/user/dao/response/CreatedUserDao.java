package com.findme.user.dao.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreatedUserDao {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("username")
    private String username;
    @JsonProperty("phoneNumber")
    private String phoneNumber;

    public CreatedUserDao(Long id, String email, String username, String phoneNumber) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
    }
}
