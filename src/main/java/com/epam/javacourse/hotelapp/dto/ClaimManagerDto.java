package com.epam.javacourse.hotelapp.dto;

import java.time.LocalDate;

/**
 * Show all applications in manager account
 */
public class ClaimManagerDto {

    private int id;
    private String bookedByUser;
    private String bookedByUserEmail;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private String roomSeats;
    private String roomClass;

    public ClaimManagerDto() {
    }

    public ClaimManagerDto(int id, String bookedByUser, String bookedByUserEmail,
                           LocalDate checkinDate, LocalDate checkoutDate, String roomSeats, String roomClass) {
        this.id = id;
        this.bookedByUser = bookedByUser;
        this.bookedByUserEmail = bookedByUserEmail;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.roomSeats = roomSeats;
        this.roomClass = roomClass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookedByUser() {
        return bookedByUser;
    }

    public void setBookedByUser(String bookedByUser) {
        this.bookedByUser = bookedByUser;
    }

    public String getBookedByUserEmail() {
        return bookedByUserEmail;
    }

    public void setBookedByUserEmail(String bookedByUserEmail) {
        this.bookedByUserEmail = bookedByUserEmail;
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

    public String getRoomSeats() {
        return roomSeats;
    }

    public void setRoomSeats(String roomSeats) {
        this.roomSeats = roomSeats;
    }

    public String getRoomClass() {
        return roomClass;
    }

    public void setRoomClass(String roomClass) {
        this.roomClass = roomClass;
    }
}
