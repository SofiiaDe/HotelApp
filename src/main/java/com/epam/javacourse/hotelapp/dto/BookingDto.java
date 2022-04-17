package com.epam.javacourse.hotelapp.dto;

import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.utils.validation.customannotations.BookingDtoRangeCheck;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@BookingDtoRangeCheck(message = "{dates.notequal.notoverlap}")
public class BookingDto {

    private int id;
    private int userId;

    @Future(message = "{checkin.future}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkin;

    @Future(message = "{checkout.future}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkout;

    private int roomId;
    private int claimId;
    private User user;

    private boolean bookingStatus;

    public BookingDto() {
    }

    public BookingDto(int id, int userId, LocalDate checkin, LocalDate checkout,
                      int roomId, int claimId, boolean bookingStatus) {
        this.id = id;
        this.userId = userId;
        this.checkin = checkin;
        this.checkout = checkout;
        this.roomId = roomId;
        this.claimId = claimId;
        this.bookingStatus = bookingStatus;
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

    public LocalDate getCheckin() {
        return checkin;
    }

    public void setCheckin(LocalDate checkin) {
        this.checkin = checkin;
    }

    public LocalDate getCheckout() {
        return checkout;
    }

    public void setCheckout(LocalDate checkout) {
        this.checkout = checkout;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(boolean bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
