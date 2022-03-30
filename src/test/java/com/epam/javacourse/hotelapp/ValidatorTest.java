package com.epam.javacourse.hotelapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

import static com.epam.javacourse.hotelapp.utils.validation.Validator.dateParameterToLocalDate;
import static com.epam.javacourse.hotelapp.utils.validation.Validator.isCorrectDate;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidatorTest {

    @Mock
    HttpServletRequest request;

    @Test
    void testDateParameterToLocalDate_correctInput() {
        String date = "2022-03-25";
        LocalDate expected = LocalDate.of(2022, 3, 25);
        assertEquals(expected, dateParameterToLocalDate(date));
    }

    @Test
    void testDateParameterToLocalDate_throwsDateTimeParseException() {
        String date = "2022-03-25aaaa";
        assertNull(dateParameterToLocalDate(date));
    }

    @Test
    void testIsCorrectDate_nullCheckinCase() {
        assertFalse(isCorrectDate(null, LocalDate.now(), request));
    }

    @Test
    void testIsCorrectDate_nullCheckoutCase() {
        assertFalse(isCorrectDate(LocalDate.now(), null, request));
    }

    @Test
    void testIsCorrectDate_checkinIsAfterCheckoutCase() {
        assertFalse(isCorrectDate(LocalDate.now().plusDays(5), LocalDate.now().plusDays(3), request));
    }

    @Test
    void testIsCorrectDate_checkinIsBeforeNowCase() {
        assertFalse(isCorrectDate(LocalDate.now().minusDays(4), LocalDate.now().plusDays(1), request));
    }

    @Test
    void testIsCorrectDate_checkoutIsBeforeNowCase() {
        assertFalse(isCorrectDate(LocalDate.now().plusDays(3), LocalDate.now().minusDays(1), request));
    }

    @Test
    void testIsCorrectDate_validDatesCase() {
        assertTrue(isCorrectDate(LocalDate.now().plusDays(4), LocalDate.now().plusDays(5), request));
    }
}
