package com.epam.javacourse.hotelapp.service.interfaces;

import com.epam.javacourse.hotelapp.dto.InvoiceClientDto;
import com.epam.javacourse.hotelapp.dto.InvoiceDto;
import com.epam.javacourse.hotelapp.dto.InvoiceManagerDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IInvoiceService {

//    void createInvoice(Invoice invoice) throws AppException;
//
//    List<Invoice> getAllInvoices() throws AppException;

    List<InvoiceManagerDto> getAllDetailedInvoices() throws AppException;

//    List<Invoice> getInvoicesByUserId(int userId) throws AppException;

    List<InvoiceClientDto> getUserDetailedInvoices(int userID) throws AppException;

    LocalDate getInvoiceDueDate(InvoiceDto invoice);

    List<InvoiceDto> getInvoicesByBookingsIds(List<Integer> bookingsIds) throws AppException;

//    /**
//     * Updates invoice's status to 'cancelled' in case of not paying the invoice by the due date.
//     * @throws AppException
//     */
//    void updateInvoiceStatusToCancelled() throws AppException;
//
//    List<Invoice> getInvoicesByStatus(String status) throws AppException;
//
//    /**
//     * Updates invoice's status to 'paid' in case of successful payment transaction.
//     * @param invoiceId
//     * @throws AppException
//     */
//    void payInvoice(int invoiceId) throws AppException;
//
//    Invoice getInvoiceById(int invoiceId) throws AppException;
}
