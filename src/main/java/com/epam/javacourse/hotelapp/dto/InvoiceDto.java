package com.epam.javacourse.hotelapp.dto;

import com.epam.javacourse.hotelapp.model.Booking;
import com.epam.javacourse.hotelapp.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvoiceDto {

    private int id;
    private int userId;
    private BigDecimal amount;
    private int bookingId;
    private LocalDate invoiceDate;
    private String status;
    private User user;
    private Booking booking;

    public InvoiceDto() {
    }

    public InvoiceDto(int id, int userId, BigDecimal amount, int bookingId,
                      LocalDate invoiceDate, String status) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.bookingId = bookingId;
        this.invoiceDate = invoiceDate;
        this.status = status;
    }

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
