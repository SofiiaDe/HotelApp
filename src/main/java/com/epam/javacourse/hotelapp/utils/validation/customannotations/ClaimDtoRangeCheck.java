package com.epam.javacourse.hotelapp.utils.validation.customannotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Custom annotation to check whether claim's check-in date is before check-out date
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Constraint(validatedBy = ClaimDtoDateValidator.class)
@Documented
public @interface ClaimDtoRangeCheck {


    String message() default "Invalid Dates";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}




