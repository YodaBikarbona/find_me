package com.findme.profileimage.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
public class IsOversizeValidator implements ConstraintValidator<IsOversize, MultipartFile> {

    @Override
    public void initialize(IsOversize constraintAnnotation) {
        // Not used
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        final int maxSize = (10 * (1024 * 1024));
        return !(file.getSize() > maxSize);
    }

}
