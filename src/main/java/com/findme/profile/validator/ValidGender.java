package com.findme.profile.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = GenderValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidGender {

    String message() default "Gender can be only male or female!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
