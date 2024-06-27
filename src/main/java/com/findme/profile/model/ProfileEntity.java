package com.findme.profile.model;

import com.findme.base.model.BaseEntity;
import com.findme.profileimage.model.ProfileImageEntity;
import com.findme.user.model.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
public class ProfileEntity extends BaseEntity {

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "gender")
    @Setter(AccessLevel.NONE)
    private Gender gender;

    @NotNull
    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "about_me")
    private String aboutMe;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Setter(AccessLevel.NONE)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "profile_image_id")
    private ProfileImageEntity profileImage;

    @OneToMany(mappedBy = "following")
    private List<FollowingEntity> followers;

    // Constructors
    public ProfileEntity(String firstName, String lastName, Gender gender, LocalDate birthday, String aboutMe, UserEntity user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.aboutMe = aboutMe;
        this.user = user;
    }

}
