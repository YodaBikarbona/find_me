package com.findme.user.validator;

import com.findme.user.model.UserEntity;
import com.findme.user.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber, String> {

    private final UserRepository repository;

    @Override
    public void initialize(UniquePhoneNumber constraintAnnotation) {
        // Not used
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (Objects.isNull(phoneNumber) || phoneNumber.isEmpty()) {
            return Boolean.TRUE;
        }
        Optional<UserEntity> user = repository.findByPhoneNumber(phoneNumber);
        return user.isEmpty();
    }
}
