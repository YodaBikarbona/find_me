package com.findme.user.validator;

import com.findme.user.dto.request.RequestNewUserDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        // Not used
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        RequestNewUserDto newUserDto = (RequestNewUserDto) object;
        return newUserDto.getPassword().equals(newUserDto.getConfirmPassword());
    }
}
