package com.findme.user.model;

import com.findme.base.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity {

    @NotNull
    @Column(name = "username")
    private String username;
    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;
    @NotNull
    @Column(name = "activated")
    private boolean activated;
    @NotNull
    @Column(name = "banned")
    private boolean banned;

    @PrePersist
    protected void onCreate() {
        activated = Boolean.FALSE;
        banned = Boolean.FALSE;
    }

    // Constructors
    public UserEntity(String username, String phoneNumber) {
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isActivated() {
        return activated;
    }

    public boolean isBanned() {
        return banned;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
