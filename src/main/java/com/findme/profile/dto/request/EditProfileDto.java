package com.findme.profile.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EditProfileDto(@NotNull @NotBlank String firstName, @NotNull @NotBlank String lastName,
                             @NotNull LocalDate birthday, String aboutMe) {

}
