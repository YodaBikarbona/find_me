package com.findme.user.validator;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;

import static com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED;

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
        return phoneNumberUtil.isValidNumberForRegion(phoneNumberUtil.parse(phoneNumber, UNSPECIFIED.name()), REGION_CODE);
    }
}
