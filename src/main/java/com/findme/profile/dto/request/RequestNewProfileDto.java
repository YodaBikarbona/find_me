package com.findme.profile.dto.request;

import com.findme.profile.model.Gender;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;

public class RequestNewProfileDto {
    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthday;
    @Null
    private String aboutMe;

    // Constructors
    public RequestNewProfileDto(String firstName, String lastName, String gender, LocalDate birthday, String aboutMe) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = Gender.findByName(gender);
        this.birthday = birthday;
        this.aboutMe = aboutMe;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getAboutMe() {
        return aboutMe;
    }

}
