package com.epam.javacourse.hotelapp.dto;

import com.epam.javacourse.hotelapp.utils.validation.customannotations.PasswordMatches;

import java.time.LocalDate;

@PasswordMatches
public class ApplicationDto {

    private int id;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private String roomTypeBySeats;
    private String roomClass;

    // For client account
    public ApplicationDto(int id, LocalDate checkinDate, LocalDate checkoutDate,
                                   String roomTypeBySeats, String roomClass) {
        this.id = id;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.roomTypeBySeats = roomTypeBySeats;
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

    public String getRoomTypeBySeats() {
        return roomTypeBySeats;
    }

    public String getRoomClass() {
        return roomClass;
    }
}
