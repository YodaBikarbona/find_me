package com.findme.user.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniquePhoneNumberValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniquePhoneNumber {

    String message() default "Invalid phone number!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
