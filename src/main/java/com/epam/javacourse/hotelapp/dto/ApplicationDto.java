package com.epam.javacourse.hotelapp.dto;

import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.utils.validation.customannotations.PasswordMatches;

import java.time.LocalDate;

@PasswordMatches
public class ApplicationDto {

    private int id;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private String roomTypeBySeats;
    private String roomClass;
    private String bookedByUser;
    private String bookedByUserEmail;
    private int userId;

    // For client account
    public ApplicationDto(int id, LocalDate checkinDate, LocalDate checkoutDate,
                                   String roomTypeBySeats, String roomClass) {
        this.id = id;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.roomTypeBySeats = roomTypeBySeats;
        this.roomClass = roomClass;
    }

    // For manager account
    public ApplicationDto(int id, String bookedByUser, String bookedByUserEmail,
                               LocalDate checkinDate, LocalDate checkoutDate, String roomTypeBySeats, String roomClass) {
        this.id = id;
        this.bookedByUser = bookedByUser;
        this.bookedByUserEmail = bookedByUserEmail;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.roomTypeBySeats = roomTypeBySeats;
        this.roomClass = roomClass;
    }

    public ApplicationDto() {

    }

    public String getBookedByUser() {
        return bookedByUser;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setCheckinDate(LocalDate checkinDate) {
        this.checkinDate = checkinDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public void setRoomTypeBySeats(String roomTypeBySeats) {
        this.roomTypeBySeats = roomTypeBySeats;
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

    public String getRoomTypeBySeats() {
        return roomTypeBySeats;
    }

    public String getRoomClass() {
        return roomClass;
    }
}
