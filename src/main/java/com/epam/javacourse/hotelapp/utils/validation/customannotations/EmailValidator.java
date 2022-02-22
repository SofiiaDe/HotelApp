package com.epam.javacourse.hotelapp.utils.validation.customannotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.epam.javacourse.hotelapp.utils.Constants.EMAIL_REGEX;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public boolean isValid(final String username, final ConstraintValidatorContext context) {
        return (validateEmail(username));
    }

    private boolean validateEmail(final String email) {
        Matcher matcher = PATTERN.matcher(email);
        return matcher.matches();
    }
}
