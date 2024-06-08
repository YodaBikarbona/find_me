package com.findme.security.model;

import com.findme.base.model.BaseEntity;
import com.findme.utils.ApplicationCtxHolderUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Base64;

@Entity
@Table(name = "users_security")
@Getter
@Setter
public class SecurityEntity extends BaseEntity {

    @NotNull
    @Column(name = "access_token_secret")
    @Setter(AccessLevel.NONE)
    private String accessTokenSecret;
    @NotNull
    @Column(name = "refresh_token_secret")
    @Setter(AccessLevel.NONE)
    private String refreshTokenSecret;
    @NotNull
    @Column(name = "password")
    private String password;

    @PrePersist
    protected void onCreate() {
        generateTokenSecret();
    }

    // Constructors
    public SecurityEntity(String password) {
        this.password = encryptPassword(password);
    }

    public SecurityEntity() { }

    // Public methods
    public void generateTokenSecret() {
        byte[] accessTokenBytes = new byte[128];
        SecureRandom randomAccess = new SecureRandom();
        randomAccess.nextBytes(accessTokenBytes);
        byte[] refreshTokenBytes = new byte[128];
        SecureRandom randomRefresh = new SecureRandom();
        randomRefresh.nextBytes(refreshTokenBytes);
        this.accessTokenSecret = Base64.getEncoder().encodeToString(accessTokenBytes);
        this.refreshTokenSecret = Base64.getEncoder().encodeToString(refreshTokenBytes);
    }

    public String encryptPassword(String password) {
        return ApplicationCtxHolderUtil.getBean(PasswordEncoder.class)
                .encode(password);
    }

}
