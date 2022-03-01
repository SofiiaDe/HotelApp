package com.epam.javacourse.hotelapp.dto;

import com.epam.javacourse.hotelapp.model.User;

import java.time.LocalDate;

public class BookingDto {

    private int id;
    private int userId;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private int roomId;
    private int claimId;
    private User user;

    public BookingDto() {
    }

    public BookingDto(int id, int userId, LocalDate checkinDate, LocalDate checkoutDate,
                      int roomId, int claimId) {
        this.id = id;
        this.userId = userId;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.roomId = roomId;
        this.claimId = claimId;
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

    public LocalDate getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(LocalDate checkinDate) {
        this.checkinDate = checkinDate;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
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
}
