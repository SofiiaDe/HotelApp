package com.epam.javacourse.hotelapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "rooms")
public class Room implements Serializable {

    private static final long serialVersionUID = -1L;

    @Id
    @GeneratedValue
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private int id;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "room_number", unique=true)
    private int roomNumber;

    @Column(name = "room_seats")
    @Enumerated(EnumType.ORDINAL)
    private String roomTypeBySeats;

    @Column(name = "room_class")
    @Enumerated(EnumType.ORDINAL)
    private String roomClass;

    @Column(name = "room_status")
    @Enumerated(EnumType.ORDINAL)
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

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

}
