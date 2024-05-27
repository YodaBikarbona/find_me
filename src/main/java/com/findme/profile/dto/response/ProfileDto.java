package com.findme.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.findme.profile.model.Gender;

import java.time.LocalDate;

public class ProfileDto {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("gender")
    private Gender gender;
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("aboutMe")
    private String aboutMe;

    public ProfileDto(Long id, String firstName, String lastName, Gender gender, LocalDate birthday, String aboutMe) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday.toString();
        this.aboutMe = aboutMe;
    }

}
