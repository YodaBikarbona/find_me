package com.findme.user.dto.request;

import com.findme.user.validator.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.Objects;

@Getter
@PasswordMatches
public class RequestNewUserDto {

    @Email
    @UniqueEmail
    private final String email;
    @UniqueUsername
    @NotBlank
    private final String username;
    @ValidPhoneNumber
    @UniquePhoneNumber
    private final String phoneNumber;
    @ValidPassword
    private final String password;
    private final String confirmPassword;

    // Constructors
    public RequestNewUserDto(String email, String username, String phoneNumber, String password, String confirmedPassword) {
        this.email = email;
        this.username = Objects.nonNull(username) ? username.trim() : null;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.confirmPassword = confirmedPassword;
    }

}
