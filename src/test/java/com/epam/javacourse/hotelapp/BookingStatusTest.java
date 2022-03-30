package com.epam.javacourse.hotelapp;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.epam.javacourse.hotelapp.utils.Helpers.calculateBookingStatus;
import static com.epam.javacourse.hotelapp.utils.enums.BookingStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingStatusTest {

    @Test
    void testCalculateBookingStatus_returnPAID() {
        LocalDate checkinDate = LocalDate.now().plusDays(2);
        LocalDate checkoutDate = LocalDate.now().plusDays(4);
        assertEquals(PAID, calculateBookingStatus(checkinDate, checkoutDate, true));
    }

    @Test
    void testCalculateBookingStatus_returnONGOING() {
        LocalDate checkinDate = LocalDate.now().minusDays(2);
        LocalDate checkoutDate = LocalDate.now().plusDays(6);
        assertEquals(ONGOING, calculateBookingStatus(checkinDate, checkoutDate, true));
    }

    @Test
    void testCalculateBookingStatus_returnFINISHED() {
        LocalDate checkinDate = LocalDate.now().minusDays(9);
        LocalDate checkoutDate = LocalDate.now().minusDays(7);
        assertEquals(FINISHED, calculateBookingStatus(checkinDate, checkoutDate, true));
    }

    @Test
    void testCalculateBookingStatus_returnNEW() {
        LocalDate checkinDate = LocalDate.now().plusDays(3);
        LocalDate checkoutDate = LocalDate.now().plusDays(4);
        assertEquals(NEW, calculateBookingStatus(checkinDate, checkoutDate, false));
    }

    @Test
    void testCalculateBookingStatus_returnCANCELLED() {
        LocalDate checkinDate = LocalDate.now().minusDays(3);
        LocalDate checkoutDate = LocalDate.now().minusDays(2);
        assertEquals(CANCELLED, calculateBookingStatus(checkinDate, checkoutDate, false));
    }
}
