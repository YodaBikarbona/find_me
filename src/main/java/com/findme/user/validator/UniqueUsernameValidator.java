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
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserRepository repository;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        // Not used
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        Optional<UserEntity> user = repository.findByUsername(username);
        return user.isEmpty();
    }
}
