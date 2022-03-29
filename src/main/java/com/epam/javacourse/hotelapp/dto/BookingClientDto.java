package com.epam.javacourse.hotelapp.dto;

import com.epam.javacourse.hotelapp.utils.Helpers;
import com.epam.javacourse.hotelapp.utils.enums.BookingStatus;

import java.time.LocalDate;

public class BookingClientDto {

    private int id;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private String roomSeats;
    private String roomClass;
    private boolean isPaid;

    public BookingClientDto(int id, LocalDate checkinDate, LocalDate checkoutDate,
                            String roomSeats, String roomClass, boolean isPaid) {
        this.id = id;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.roomSeats = roomSeats;
        this.roomClass = roomClass;
        this.isPaid = isPaid;
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

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public boolean isPaid() {
        return isPaid;
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

    public boolean getIsPaid() {
        return isPaid;
    }

    public BookingStatus getBookingStatus(){
        return Helpers.calculateBookingStatus(getCheckinDate(), getCheckoutDate(), getIsPaid());
    }
}
