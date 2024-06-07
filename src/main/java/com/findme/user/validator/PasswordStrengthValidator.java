package com.findme.user.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class PasswordStrengthValidator implements ConstraintValidator<ValidPassword, String> {

    private final PasswordValidator passwordValidator;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        // Not used
    }

    @Override
    @SneakyThrows
    public boolean isValid(String password, ConstraintValidatorContext context) {
        RuleResult result = passwordValidator.validate(new PasswordData(password));

        boolean valid = result.isValid();
        if (!valid) {
            List<String> messages = passwordValidator.getMessages(result);
            customMessageForValidation(context, messages);
        }
        return valid;
    }

    private void customMessageForValidation(ConstraintValidatorContext context, List<String> messages) {
        context.disableDefaultConstraintViolation();
        messages.forEach(message -> context.buildConstraintViolationWithTemplate(message).addConstraintViolation());
    }
}
