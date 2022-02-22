package com.epam.javacourse.hotelapp.utils.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.epam.javacourse.hotelapp.utils.Constants.EMAIL_REGEX;
import static com.epam.javacourse.hotelapp.utils.Constants.PASSWORD_REGEX;
import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat.EMAIL;

public class Validator {

    public static boolean isEmailValid(String email, int maxLength) {

        return email != null && email.length() <= maxLength && Pattern.compile(EMAIL_REGEX)
                .matcher(email)
                .matches();
    }

    /**
     *
     * @param password password entered by a user
     * @param minLength min length of the password established in a regex
     * @param maxLength max length of the password variable in a database
     * @return message to a user explaining incorrect input or null if validation was successful
     */
    public static boolean isPasswordValid(String password, int minLength, int maxLength) {

        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);

        return password.length() <= maxLength && password.length() >= minLength && matcher.find();
    }
}
