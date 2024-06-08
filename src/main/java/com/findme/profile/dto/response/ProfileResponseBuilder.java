package com.findme.profile.dto.response;

import com.findme.profile.model.Gender;
import com.findme.profileimage.dto.response.ProfileImageDto;

import java.time.LocalDate;

public class ProfileResponseBuilder {

    private Long id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String birthday;
    private String aboutMe;
    private ProfileImageDto thumbnail;

    ProfileResponseBuilder() {
    }

    public ProfileResponseBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ProfileResponseBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public ProfileResponseBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public ProfileResponseBuilder gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public ProfileResponseBuilder birthday(LocalDate birthday) {
        this.birthday = birthday.toString();
        return this;
    }

    public ProfileResponseBuilder aboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
        return this;
    }

    public ProfileResponseBuilder thumbnail(ProfileImageDto thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public ProfileDto build() {
        return new ProfileDto(id,
                firstName,
                lastName,
                gender.getName(),
                birthday,
                aboutMe,
                thumbnail);
    }
}
