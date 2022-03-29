package com.epam.javacourse.hotelapp.service.interfaces;

import com.epam.javacourse.hotelapp.dto.BookingDto;
import com.epam.javacourse.hotelapp.dto.InvoiceDto;
import com.epam.javacourse.hotelapp.exception.AppException;

import java.math.BigDecimal;

public interface IBookingInvoiceService {

    /**
     * Creates new invoice once the new booking appeared.
     */
    void createBookingAndInvoice(BookingDto booking, InvoiceDto invoice) throws AppException;

    BigDecimal getInvoiceAmount(BookingDto booking) throws AppException;

    void cancelUnpaidBookings() throws AppException;

    /**
     * Updates invoice's status to 'paid' in case of successful payment transaction.
     */
    void payInvoice(int invoiceId) throws AppException;
}
