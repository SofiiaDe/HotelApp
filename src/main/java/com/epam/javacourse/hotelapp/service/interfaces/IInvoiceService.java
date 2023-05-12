package com.epam.javacourse.hotelapp.service.interfaces;

import com.epam.javacourse.hotelapp.dto.InvoiceClientDto;
import com.epam.javacourse.hotelapp.dto.InvoiceDto;
import com.epam.javacourse.hotelapp.dto.InvoiceManagerDto;
import com.epam.javacourse.hotelapp.exception.AppException;

import java.util.List;

public interface IInvoiceService {

    List<InvoiceManagerDto> getAllDetailedInvoices() throws AppException;

    List<InvoiceClientDto> getUserDetailedInvoices(int userID) throws AppException;

    List<InvoiceDto> getInvoicesByBookingsIds(List<Integer> bookingsIds) throws AppException;

    /**
     * Updates invoice's status to 'cancelled' in case of not paying the invoice by the due date.
     *
     */
    void updateInvoiceStatusToCancelled() throws AppException;

    List<InvoiceDto> getInvoicesByStatus(String status) throws AppException;

    InvoiceDto getInvoiceById(int invoiceId);
}
