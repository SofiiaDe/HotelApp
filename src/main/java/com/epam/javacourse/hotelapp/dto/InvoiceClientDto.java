package com.epam.javacourse.hotelapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO to show list of Client's invoices in Client's account
 */
public class InvoiceClientDto {

    private final int id;
    private final LocalDate invoiceDate;
    private final LocalDate dueDate;
    private final BigDecimal amount;
    private final int bookingId;
    private final BigDecimal pricePerNight;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final String status;

    public InvoiceClientDto(int id, LocalDate invoiceDate, LocalDate dueDate,
                            BigDecimal amount, int bookingId, BigDecimal pricePerNight, LocalDate checkInDate,
                            LocalDate checkOutDate, String status) {
        this.id = id;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.amount = amount;
        this.bookingId = bookingId;
        this.pricePerNight = pricePerNight;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public int getBookingId() {
        return bookingId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public String getStatus() {
        return status;
    }
}
