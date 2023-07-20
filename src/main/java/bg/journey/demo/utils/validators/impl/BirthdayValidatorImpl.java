package bg.journey.demo.utils.validators.impl;

import bg.journey.demo.dto.payload.SingupDTO;
import bg.journey.demo.utils.validators.BirthdayValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;


public class BirthdayValidatorImpl implements ConstraintValidator<BirthdayValidator, SingupDTO> {

    @Override
    public void initialize(BirthdayValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SingupDTO dto, ConstraintValidatorContext context) {
        if (dto.getBirthdate() == null) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Birthday field is required.")
                    .addConstraintViolation();
            return false;
        } else if (LocalDateTime.now().getYear() - dto.getBirthdate().getYear() < 14) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("You must be above 14 years.")
                    .addConstraintViolation();
            return false;
        } else if (dto.getBirthdate().getMonthValue() > 12) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Invalid month.")
                    .addConstraintViolation();
            return false;
        } else if (dto.getBirthdate().getDayOfMonth() > 31) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Invalid day.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}