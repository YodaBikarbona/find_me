package com.findme.user.model;

import com.findme.base.model.BaseEntity;
import com.findme.secutiry.model.SecurityEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @NotNull
    @Column(name = "email")
    private String email;
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

    @ManyToOne
    @JoinColumn(name = "security_id")
    private SecurityEntity security;

    @PrePersist
    protected void onCreate() {
        activated = Boolean.FALSE;
        banned = Boolean.FALSE;
    }

    // Constructors
    public UserEntity(String email, String username, String phoneNumber) {
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    public UserEntity() { }

    // Getters
    public String getEmail() {
        return email;
    }

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
    public void setEmail(String email) {
        this.email = email;
    }

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

    public void setSecurity(SecurityEntity security) {
        this.security = security;
    }
}
