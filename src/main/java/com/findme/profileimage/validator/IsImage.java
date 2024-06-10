package com.findme.profileimage.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsImageValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsImage {

    String message() default "The file is not an image!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
