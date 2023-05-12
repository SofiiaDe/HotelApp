package com.epam.javacourse.hotelapp.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class RoomDto {


    private int id;

    @DecimalMin(value = "100.00", message = "Room price should correspond to the company's pricing policy.")
    @DecimalMax(value = "999.00", message = "Room price should correspond to the company's pricing policy.")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal price;

    @Min(value = 1, message = "Room number can't be assigned a negative value")
    private int roomNumber;

    private String roomSeats;

    private String roomClass;

    private String roomStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
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

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }
}
