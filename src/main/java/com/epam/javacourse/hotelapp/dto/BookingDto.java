package com.epam.javacourse.hotelapp.dto;

import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.utils.Helpers;
import com.epam.javacourse.hotelapp.utils.enums.BookingStatus;

import java.time.LocalDate;

public class BookingDto {

    private int id;
    private String bookedByUser;
    private String bookedByUserEmail;
    private int userId;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private String roomTypeBySeats;
    private String roomClass;
    private boolean isPaid;
    private int roomNumber;


    // For User account
    public  BookingDto(int id, LocalDate checkinDate, LocalDate checkoutDate,
                               String roomTypeBySeats, String roomClass, boolean isPaid) {
        this.id = id;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.roomTypeBySeats = roomTypeBySeats;
        this.roomClass = roomClass;
        this.isPaid = isPaid;
    }

    // For Manager account
    public BookingDto(int id, String bookedByUser, String bookedByUserEmail,
                           LocalDate checkinDate, LocalDate checkoutDate, int roomNumber, boolean isPaid) {
        this.id = id;
        this.bookedByUser = bookedByUser;
        this.bookedByUserEmail = bookedByUserEmail;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.roomNumber = roomNumber;
        this.isPaid = isPaid;
    }

    public BookingDto() {
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

    public void setBookedByUser(String bookedByUser) {
        this.bookedByUser = bookedByUser;
    }

    public void setBookedByUserEmail(String bookedByUserEmail) {
        this.bookedByUserEmail = bookedByUserEmail;
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

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBookedByUser() {
        return bookedByUser;
    }

    public String getBookedByUserEmail() {
        return bookedByUserEmail;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public int getRoomNumber() {
        return roomNumber;
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

    public boolean getIsPaid() {
        return isPaid;
    }

    public BookingStatus getBookingStatus(){
        return Helpers.calculateBookingStatus(getCheckinDate(), getCheckoutDate(), getIsPaid());
    }
}
