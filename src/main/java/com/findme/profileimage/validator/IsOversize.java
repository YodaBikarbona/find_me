package com.findme.profileimage.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsOversizeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsOversize {

    String message() default "The image is too big!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
