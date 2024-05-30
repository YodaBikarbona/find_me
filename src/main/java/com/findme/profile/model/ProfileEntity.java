package com.findme.profile.model;

import com.findme.base.model.BaseEntity;
import com.findme.profileimage.model.ProfileImageEntity;
import com.findme.user.model.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "profiles")
public class ProfileEntity extends BaseEntity {

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "gender")
    private Gender gender;

    @NotNull
    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "about_me")
    private String aboutMe;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "profile_image_id")
    private ProfileImageEntity profileImage;

    // Constructors
    public ProfileEntity(String firstName, String lastName, Gender gender, LocalDate birthday, String aboutMe, UserEntity user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.aboutMe = aboutMe;
        this.user = user;
    }

    public ProfileEntity() { }

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

    public UserEntity getUser() {
        return user;
    }

    public ProfileImageEntity getProfileImage() {
        return profileImage;
    }

    // Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setProfileImage(ProfileImageEntity profileImage) {
        this.profileImage = profileImage;
    }

}
