package com.epam.javacourse.hotelapp.dto;

import com.epam.javacourse.hotelapp.utils.Helpers;
import com.epam.javacourse.hotelapp.utils.enums.BookingStatus;

import java.time.LocalDate;

/**
 * DTO to show all bookings in Manager's account
 */
public class BookingManagerDto {

    private int id;
    private String bookedByUser;
    private String bookedByUserEmail;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private String roomSeats;
    private String roomClass;
    private int roomNumber;
    private boolean isPaid;

    public BookingManagerDto(int id, String bookedByUser, String bookedByUserEmail,
                            LocalDate checkinDate, LocalDate checkoutDate, int roomNumber, boolean isPaid) {
        this.id = id;
        this.bookedByUser = bookedByUser;
        this.bookedByUserEmail = bookedByUserEmail;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.roomNumber = roomNumber;
        this.isPaid = isPaid;
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

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public BookingStatus getBookingStatus(){
        return Helpers.calculateBookingStatus(getCheckinDate(), getCheckoutDate(), getIsPaid());
    }
}
