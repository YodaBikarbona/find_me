package com.findme.user.dto.request;

import com.findme.user.validator.UniqueUsername;
import com.findme.user.validator.ValidPassword;
import com.findme.user.validator.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.Objects;

@Getter
public class RequestNewUserDto {

    @Email
    private final String email;
    @UniqueUsername
    @NotBlank
    private final String username;
    @ValidPhoneNumber
    private final String phoneNumber;
    @ValidPassword
    private final String password;

    // Constructors
    public RequestNewUserDto(String email, String username, String phoneNumber, String password, String confirmedPassword) {
        if (!password.equals(confirmedPassword)) {
            throw new IllegalArgumentException("The passwords must be same!");
        }
        this.email = email;
        this.username = Objects.nonNull(username) ? username.trim() : null;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

}
