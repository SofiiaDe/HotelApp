package com.epam.javacourse.hotelapp.dto;

import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.utils.validation.customannotations.BookingDtoRangeCheck;
import com.epam.javacourse.hotelapp.utils.validation.customannotations.ClaimDtoRangeCheck;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import java.time.LocalDate;

@BookingDtoRangeCheck(message = "Check-in date should be earlier than check-out date. Dates can't be overlapping")
public class BookingDto {

    private int id;
    private int userId;

    @Future(message = "Check-in date can't be earlier than current date. " +
            "Please enter correct date.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkin;

    @Future(message = "Check-out date can't be earlier than current date. " +
            "Please enter correct date.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkout;

    private int roomId;
    private int claimId;
    private User user;

    private boolean status;

    public BookingDto() {
    }

    public BookingDto(int id, int userId, LocalDate checkin, LocalDate checkout,
                      int roomId, int claimId, boolean status) {
        this.id = id;
        this.userId = userId;
        this.checkin = checkin;
        this.checkout = checkout;
        this.roomId = roomId;
        this.claimId = claimId;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
