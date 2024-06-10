package com.findme.user.validator;

import com.findme.exceptions.BadRequestException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import static com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED;

@Component
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private static final String REGION_CODE = "HR";

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        // Not used
    }

    @SneakyThrows
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (!phoneNumber.matches("^\\+?\\d+$")) {
            throw new  BadRequestException("The phone number format is invalid!");
        }
        return phoneNumberUtil.isValidNumberForRegion(phoneNumberUtil.parse(phoneNumber, UNSPECIFIED.name()), REGION_CODE);
    }
}
