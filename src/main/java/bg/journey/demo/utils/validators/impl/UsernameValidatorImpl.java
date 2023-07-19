package bg.journey.demo.utils.validators.impl;

import bg.journey.demo.utils.validators.UsernameValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static bg.journey.demo.config.AppConstants.MAXIMUM_USERNAME_LENGTH;
import static bg.journey.demo.config.AppConstants.MINIMUM_USERNAME_LENGTH;


public class UsernameValidatorImpl implements ConstraintValidator<UsernameValidator, String> {
    private String regex = "^(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";

    @Override
    public void initialize(UsernameValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.length() >= MINIMUM_USERNAME_LENGTH && value.length() <= MAXIMUM_USERNAME_LENGTH
               && value.matches(regex);
    }
}