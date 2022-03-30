package com.epam.javacourse.hotelapp.utils.validation.customannotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Constraint(validatedBy = ClaimDtoDateValidator.class)
@Documented
public @interface ClaimDtoRangeCheck {


    String message() default "Invalid Dates";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}




