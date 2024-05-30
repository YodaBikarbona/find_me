package com.findme.profileimage.model;

import com.findme.base.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity(name = "profile_images")
public class ProfileImageEntity extends BaseEntity {

    @NotNull
    @Column(name = "url")
    private String url;

    // Constructors
    public ProfileImageEntity(String url) {
        this.url = url;
    }

    public ProfileImageEntity() { }

    // Getters
    public String getUrl() {
        return url;
    }

    // Setters
    public void setUrl(String url) {
        this.url = url;
    }

}
