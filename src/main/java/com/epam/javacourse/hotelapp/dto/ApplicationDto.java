package com.epam.javacourse.hotelapp.dto;

import java.time.LocalDate;

public class ApplicationDto {

    private int id;
    private int userId;
    private String roomTypeBySeats;
    private String roomClass;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;

    public ApplicationDto() {
    }

    public ApplicationDto(int id, int userId, String roomTypeBySeats,
                          String roomClass, LocalDate checkinDate,
                          LocalDate checkoutDate) {
        this.id = id;
        this.userId = userId;
        this.roomTypeBySeats = roomTypeBySeats;
        this.roomClass = roomClass;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
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

    public String getRoomTypeBySeats() {
        return roomTypeBySeats;
    }

    public void setRoomTypeBySeats(String roomTypeBySeats) {
        this.roomTypeBySeats = roomTypeBySeats;
    }

    public String getRoomClass() {
        return roomClass;
    }

    public void setRoomClass(String roomClass) {
        this.roomClass = roomClass;
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
}
