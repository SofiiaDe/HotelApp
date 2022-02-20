package com.epam.javacourse.hotelapp.model;

import org.hibernate.annotations.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Invoice {

    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private BigDecimal amount;
    private int bookingId;
    private LocalDate invoiceDate;
    private String invoiceStatus;

    private LocalDate dueDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}

enum InvoiceStatus{
    NEW,
    PAID,
    CANCELLED,
}

