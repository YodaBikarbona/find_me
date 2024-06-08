package com.findme.profileimage.model;

import com.findme.base.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "profile_images")
@Getter
@Setter
public class ProfileImageEntity extends BaseEntity {

    @NotNull
    @Column(name = "url")
    private String url;

    // Constructors
    public ProfileImageEntity(String url) {
        this.url = url;
    }

    public ProfileImageEntity() { }

}
