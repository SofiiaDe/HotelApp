package com.epam.javacourse.hotelapp.utils;

import com.epam.javacourse.hotelapp.utils.enums.BookingStatus;

import java.time.LocalDate;

public class Helpers {

    //    public static String buildUrl(LocalDate checkinDate, LocalDate checkoutDate, int currentPage, int pageSize, String sortBy, String sortType, String roomStatus, String roomSeats) {
    public static String buildBookingPageUrl(String checkinDate, String checkoutDate, int page, String sortBy, String sortType) {

        String url = "/client/book?";
        if (page <= 0) {
            page = 1;
        }
        url = url + "page=" + page;

        if(checkinDate != null) {
            url = url + "&checkin=" + checkinDate;
        }

        if(checkoutDate != null) {
            url = url + "&checkout=" + checkoutDate;
        }

        if(sortBy != null) {
            url = url + "&sortBy=" + sortBy;
        }

        if(sortType != null) {
            url = url + "&sortType" + sortType;
        }

        return url;
    }

    public static BookingStatus calculateBookingStatus(LocalDate checkinDate, LocalDate checkoutDate, boolean isPaid) {
        if (isPaid) {
            if (LocalDate.now().isBefore(checkinDate)) {
                return BookingStatus.PAID;
            }
            if (LocalDate.now().isAfter(checkinDate) && LocalDate.now().isBefore(checkoutDate)) {
                return BookingStatus.ONGOING;
            }
            if (LocalDate.now().isAfter(checkoutDate)) {
                return BookingStatus.FINISHED;
            }
        } else {
            if (LocalDate.now().isBefore(checkinDate)) {
                return BookingStatus.NEW;
            }
            return BookingStatus.CANCELLED;
        }

        return BookingStatus.NONE;

    }
}
