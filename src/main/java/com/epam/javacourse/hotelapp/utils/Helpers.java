package com.epam.javacourse.hotelapp.utils;

import com.epam.javacourse.hotelapp.dto.InvoiceDto;
import com.epam.javacourse.hotelapp.utils.enums.BookingStatus;

import java.time.LocalDate;

public class Helpers {

    /**
     * Dynamically build URL finds and chooses room to book
     *
     * @param page     current page
     * @param sortBy   sorting criteria (like price / class)
     * @param sortType sorting order (asc / desc)
     * @return URL containing params chosen by a client
     */
    public static String buildBookingPageUrl(String checkinDate, String checkoutDate, int page, String sortBy,
                                             String sortType, String status, String seats) {

        String url = "/client/book?";
        if (page <= 0) {
            page = 1;
        }
        url = url + "page=" + page;

        if (checkinDate != null) {
            url = url + "&checkin=" + checkinDate;
        }

        if (checkoutDate != null) {
            url = url + "&checkout=" + checkoutDate;
        }

        if (sortBy != null) {
            url = url + "&sortBy=" + sortBy;
        }

        if (sortType != null) {
            url = url + "&sortType=" + sortType;
        }

        if (status != null) {
            url = url + "&status=" + status;
        }

        if (seats != null) {
            url = url + "&seats=" + seats;
        }

        return url;
    }

    /**
     * Dynamically assign status of booking to show in the User's account
     */
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

    public static LocalDate getInvoiceDueDate(InvoiceDto invoice) {
        LocalDate invoiceDate = invoice.getInvoiceDate();
        return invoiceDate.plusDays(2);

    }
}
