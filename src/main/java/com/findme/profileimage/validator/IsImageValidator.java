package com.findme.profileimage.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
public class IsImageValidator implements ConstraintValidator<IsImage, MultipartFile> {

    @Override
    public void initialize(IsImage constraintAnnotation) {
        // Not used
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        String contentType = file.getContentType();
        boolean isValid = Boolean.TRUE;
        if (contentType == null || !contentType.startsWith("image/")) {
            isValid = Boolean.FALSE;
        }
        return isValid;
    }

}
