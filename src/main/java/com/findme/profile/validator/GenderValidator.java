package com.findme.profile.validator;

import com.findme.profile.model.Gender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class GenderValidator implements ConstraintValidator<ValidGender, String> {

    @Override
    public void initialize(ValidGender constraintAnnotation) {
        // Not used
    }

    @Override
    public boolean isValid(String gender, ConstraintValidatorContext context) {
        try {
            Gender.findByName(gender);
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
