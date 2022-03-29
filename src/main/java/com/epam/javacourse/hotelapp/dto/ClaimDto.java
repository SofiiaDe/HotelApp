package com.epam.javacourse.hotelapp.dto;

import com.epam.javacourse.hotelapp.model.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import java.time.LocalDate;

public class ClaimDto {

    private int id;
    private int userId;
    private String roomSeats;
    private String roomClass;

    @Future(message = "Check-in date can't be earlier than current date. " +
            "Please enter correct dates.")
//    @NotNull(message = "Check-in date can't be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkinDate;

    @Future(message = "Check-out date can't be earlier than current date. " +
            "Please enter correct dates.")
//    @NotNull(message = "Check-out date can't be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkoutDate;

    private User user;

    public ClaimDto() {
    }

    public ClaimDto(int id, int userId, String roomSeats,
                    String roomClass, LocalDate checkinDate,
                    LocalDate checkoutDate) {
        this.id = id;
        this.userId = userId;
        this.roomSeats = roomSeats;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
