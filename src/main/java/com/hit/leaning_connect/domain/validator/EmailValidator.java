package com.hit.leaning_connect.domain.validator;

import com.hit.leaning_connect.domain.annotation.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private Pattern pattern;

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        pattern = Pattern.compile(constraintAnnotation.pattern());
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return false;
        }
        return pattern.matcher(email).matches();
    }
}