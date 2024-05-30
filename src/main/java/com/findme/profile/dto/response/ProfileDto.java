package com.findme.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.findme.profile.model.Gender;
import com.findme.profileimage.dto.response.ProfileImageDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;

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
    private Gender gender;
    @NotNull
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("aboutMe")
    private String aboutMe;
    @JsonProperty("thumbnail")
    private ProfileImageDto thumbnail;

    public ProfileDto(Long id, String firstName, String lastName, Gender gender, LocalDate birthday, String aboutMe, ProfileImageDto thumbnail) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday.toString();
        this.aboutMe = aboutMe;
        this.thumbnail = thumbnail;
    }

}
