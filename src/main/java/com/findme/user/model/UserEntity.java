package com.findme.user.model;

import com.findme.base.model.BaseEntity;
import com.findme.security.model.SecurityEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
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
    @Setter(AccessLevel.NONE)
    private Boolean activated;
    @NotNull
    @Column(name = "banned")
    @Setter(AccessLevel.NONE)
    private Boolean banned;

    @ManyToOne
    @JoinColumn(name = "security_id")
    @Setter(AccessLevel.NONE)
    private SecurityEntity security;

    @PrePersist
    protected void onCreate() {
        activated = Boolean.FALSE;
        banned = Boolean.FALSE;
    }

    // Constructors
    public UserEntity(String email, String username, String phoneNumber, SecurityEntity securityEntity) {
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.security = securityEntity;
    }

    public void activateUser() {
        this.activated = Boolean.TRUE;
    }

    public void deactivateUser() {
        this.activated = Boolean.FALSE;
    }

    public void banUser() {
        this.banned = Boolean.TRUE;
    }

}
