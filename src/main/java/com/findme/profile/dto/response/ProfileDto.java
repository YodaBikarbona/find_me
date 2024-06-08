package com.findme.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.findme.profileimage.dto.response.ProfileImageDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {

    @NotNull
    @JsonProperty("id")
    private Long id;
    @NotNull
    @JsonProperty("firstName")
    private String firstName;
    @NotNull
    @JsonProperty("lastName")
    private String lastName;
    @NotNull
    @JsonProperty("gender")
    private String gender;
    @NotNull
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("aboutMe")
    private String aboutMe;
    @JsonProperty("thumbnail")
    private ProfileImageDto thumbnail;

    public static ProfileResponseBuilder builder() {
        return new ProfileResponseBuilder();
    }
}
