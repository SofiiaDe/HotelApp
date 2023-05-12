
package com.epam.javacourse.hotelapp.utils.validation;

import com.epam.javacourse.hotelapp.exception.AppException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.epam.javacourse.hotelapp.utils.Constants.EMAIL_REGEX;
import static com.epam.javacourse.hotelapp.utils.Constants.PASSWORD_REGEX;

public class Validator {

    private static final Logger logger = LogManager.getLogger(Validator.class);


    public static boolean isEmailValid(String email, int maxLength) {

        return email != null && email.length() <= maxLength && Pattern.compile(EMAIL_REGEX)
                .matcher(email)
                .matches();
    }

    /**
     * @param password  password entered by a user
     * @param minLength min length of the password established in a regex
     * @param maxLength max length of the password variable in a database
     * @return message to a user explaining incorrect input or null if validation was successful
     */
    public static boolean isPasswordValid(String password, int minLength, int maxLength) {

        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);

        return password.length() <= maxLength && password.length() >= minLength && matcher.find();
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * validates parsing date parameter to LocalDate type
     *
     * @param date parameter of date selected by user
     *             //     * @param request HttpServletRequest request
     * @return LocalDate type of parsed date
     */
    public static LocalDate dateParameterToLocalDate(String date) {

        if (date == null || date.isEmpty()) {
            logger.error("Check-in and/or check-out dates were not selected");
        }
        LocalDate parsedDate = null;
        try {
            parsedDate = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            logger.error("Cannot get date type", e);
        }
        return parsedDate;
    }

    /**
     * Validates if checkin date is not after checkout date or checkout date is equal to checkin date
     *
     * @throws AppException in case of at least one of dates is incorrect
     */
    public static void ensureDatesAreValid(LocalDate checkinDate, LocalDate checkoutDate) throws AppException {
        if (checkinDate.isAfter(checkoutDate) || checkoutDate.isEqual(checkinDate)) {
            throw new AppException("Check-in and check-out dates are overlapping or equal");

        }
    }

    /**
     * @param checkin  checkinDate selected by user and then parsed to LocalDate type
     * @param checkout checkoutDate selected by user and then parsed to LocalDate type
     * @param request  HttpServletRequest request
     * @return true if user selected valid checkin and checkout dates
     */
    public static boolean isCorrectDate(LocalDate checkin, LocalDate checkout, HttpServletRequest request) {
        if (checkin == null || checkout == null) {
            logger.error("Check-in and/or check-out dates were not selected");
            request.setAttribute("errorMessage", "Please select check-in and check-out dates.");
            return false;
        }

        if (checkin.isAfter(checkout)) {
            logger.error("Check-in date is after check-out date");
            request.setAttribute("errorMessage", "Check-out date cannot be later than check-in date.\n " +
                    "Please enter correct dates.");
            return false;
        }

        if (checkin.isBefore(LocalDate.now()) || checkout.isBefore(LocalDate.now())) {
            logger.error("Check-in date and/or check-out date are before current date");
            request.setAttribute("errorMessage", "Check-in date and check-out date cannot be earlier than current date.\n " +
                    "Please enter correct dates.");
            return false;
        }
        return true;
    }
}
