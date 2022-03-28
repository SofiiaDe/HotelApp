package com.epam.javacourse.hotelapp.utils.validation.customannotations;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.epam.javacourse.hotelapp.utils.Constants.PASSWORD_REGEX;

@Component
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final Pattern PATTERN = Pattern.compile(PASSWORD_REGEX);

    @Override
    public boolean isValid(final String password, final ConstraintValidatorContext context) {
        return (validatePassword(password));
    }

    private boolean validatePassword(final String password) {
        Matcher matcher = PATTERN.matcher(password);
        return matcher.matches();
    }
}
