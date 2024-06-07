package com.findme.security.model;

import com.findme.base.model.BaseEntity;
import com.findme.utils.ApplicationCtxHolderUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Entity
@Table(name = "users_security")
public class SecurityEntity extends BaseEntity {

    @NotNull
    @Column(name = "access_token_secret")
    private String accessTokenSecret;
    @NotNull
    @Column(name = "refresh_token_secret")
    private String refreshTokenSecret;
    @NotNull
    @Column(name = "password")
    private String password;
    @NotNull
    @Column(name = "salt")
    private String salt;

    @PrePersist
    protected void onCreate() {
        generateTokenSecret();
    }

    // Constructors
    public SecurityEntity(String password) {
        generateSalt();
        //this.password = encryptPassword(password);
        this.password = ApplicationCtxHolderUtil.getBean(PasswordEncoder.class)
                .encode(password);
    }

    public SecurityEntity() { }

    // Getters
    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public String getRefreshTokenSecret() {
        return refreshTokenSecret;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    // Setters
    public void setPassword(String password) {
        this.password = encryptPassword(password);
    }

    // Public methods
    public void generateSalt() {
        byte[] saltBytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(saltBytes);
        this.salt = Base64.getEncoder().encodeToString(saltBytes);
    }

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
        String encryptedPassword = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(this.salt));
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            encryptedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("The password cannot be created!");
        }
        return encryptedPassword;
    }

}
