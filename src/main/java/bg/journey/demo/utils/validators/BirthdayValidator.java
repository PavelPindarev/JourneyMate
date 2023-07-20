package bg.journey.demo.utils.validators;

import bg.journey.demo.utils.validators.impl.BirthdayValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BirthdayValidatorImpl.class)
@Target({ ElementType.TYPE ,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthdayValidator {
    String message() default "Invalid birthday date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}