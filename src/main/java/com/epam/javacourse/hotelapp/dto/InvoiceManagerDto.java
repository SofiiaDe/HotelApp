package com.epam.javacourse.hotelapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO to show all invoices in Manager's account
 */
public class InvoiceManagerDto {

    private final int id;
    private final String bookedByUser;
    private final String bookedByUserEmail;
    private final BigDecimal amount;
    private final int bookingId;
    private final LocalDate invoiceDate;
    private final String status;


    public InvoiceManagerDto(int id, String bookedByUser, String bookedByUserEmail,
                             BigDecimal amount, int bookingId, LocalDate invoiceDate,
                             String status) {
        this.id = id;
        this.bookedByUser = bookedByUser;
        this.bookedByUserEmail = bookedByUserEmail;
        this.amount = amount;
        this.bookingId = bookingId;
        this.invoiceDate = invoiceDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getBookedByUser() {
        return bookedByUser;
    }

    public String getBookedByUserEmail() {
        return bookedByUserEmail;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getBookingId() {
        return bookingId;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public String getStatus() {
        return status;
    }
}
