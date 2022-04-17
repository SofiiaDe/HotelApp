package com.epam.javacourse.hotelapp.dto;

import java.time.LocalDate;

/**
 * DTO to show list of Client's claims in Client's account
 */
public class ClaimClientDto {

    private int id;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private String roomSeats;
    private String roomClass;

    public ClaimClientDto(int id, LocalDate checkinDate, LocalDate checkoutDate,
                          String roomSeats, String roomClass) {
        this.id = id;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.roomSeats = roomSeats;
        this.roomClass = roomClass;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setCheckinDate(LocalDate checkinDate) {
        this.checkinDate = checkinDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public void setRoomSeats(String roomSeats) {
        this.roomSeats = roomSeats;
    }

    public void setRoomClass(String roomClass) {
        this.roomClass = roomClass;
    }

    public int getId() {
        return id;
    }

    public LocalDate getCheckinDate() {
        return checkinDate;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public String getRoomSeats() {
        return roomSeats;
    }

    public String getRoomClass() {
        return roomClass;
    }
}
