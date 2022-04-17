package com.epam.javacourse.hotelapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;



@NamedStoredProcedureQuery(
        name="getReservedRooms",
        procedureName="reserved_rooms",
        resultClasses = { Room.class },
        parameters={
                @StoredProcedureParameter(name="checkin_date", type= LocalDate.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="checkout_date", type= LocalDate.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="room_seats", type= String.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="sortBy", type= String.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="sortType", type= String.class, mode=ParameterMode.IN)
        })
@NamedStoredProcedureQuery(
        name="getBookedRooms",
        procedureName="booked_rooms",
        resultClasses = { Room.class },
        parameters={
                @StoredProcedureParameter(name="checkin_date", type= LocalDate.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="checkout_date", type= LocalDate.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="room_seats", type= String.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="sortBy", type= String.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="sortType", type= String.class, mode=ParameterMode.IN)
        })
@NamedStoredProcedureQuery(
        name="getUnavailableRooms",
        procedureName="unavailable_rooms",
        resultClasses = { Room.class },
        parameters={
                @StoredProcedureParameter(name="checkin_date", type= LocalDate.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="checkout_date", type= LocalDate.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="room_seats", type= String.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="sortBy", type= String.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="sortType", type= String.class, mode=ParameterMode.IN)
        })
@NamedStoredProcedureQuery(
        name="getAvailableRooms",
        procedureName="available_rooms",
        resultClasses = { Room.class },
        parameters={
                @StoredProcedureParameter(name="checkin_date", type= LocalDate.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="checkout_date", type= LocalDate.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="room_seats", type= String.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="sortBy", type= String.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(name="sortType", type= String.class, mode=ParameterMode.IN)
        })
@Entity
@Table(name = "rooms")
public class Room implements Serializable {

    private static final long serialVersionUID = 5512347890045635L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private int id;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "room_number", unique=true)
    private int roomNumber;

    @Column(name = "room_seats")
    private String roomSeats;

    @Column(name = "room_class")
    private String roomClass;

    @Column(name = "room_status")
    private String roomStatus;

    public Room() {
    }



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
