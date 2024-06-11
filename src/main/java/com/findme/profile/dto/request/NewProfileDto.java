package com.findme.profile.dto.request;

import com.findme.profile.validator.ValidGender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NewProfileDto(@NotNull @NotBlank String firstName, @NotNull @NotBlank String lastName,
                            @NotNull @ValidGender String gender, @NotNull LocalDate birthday, String aboutMe) {
}
