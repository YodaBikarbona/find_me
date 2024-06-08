package com.findme.user.validator;

import com.findme.user.model.UserEntity;
import com.findme.user.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository repository;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        // Not used
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        Optional<UserEntity> user = repository.findByEmail(email);
        return user.isEmpty();
    }
}
