package com.epam.javacourse.hotelapp.model;

import org.hibernate.annotations.Entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Invoice {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private int id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "amount")
    private BigDecimal amount;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "booking_id")
    private int bookingId;

    @Column(name = "invoice_date")
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

