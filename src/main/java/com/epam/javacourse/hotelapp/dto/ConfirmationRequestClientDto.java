package com.epam.javacourse.hotelapp.dto;

import java.time.LocalDate;

public class ConfirmationRequestClientDto {

    private int id;
    private LocalDate confirmRequestDate;
    private LocalDate confirmRequestDueDate;
    private String roomTypeBySeats;
    private String roomClass;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private int claimId;
    private String status;

    public ConfirmationRequestClientDto(int id, LocalDate confirmRequestDate,
                                        LocalDate confirmRequestDueDate,
                                        String roomTypeBySeats, String roomClass,
                                        LocalDate checkinDate, LocalDate checkoutDate,
                                        int claimId, String status) {
        this.id = id;
        this.confirmRequestDate = confirmRequestDate;
        this.confirmRequestDueDate = confirmRequestDueDate;
        this.roomTypeBySeats = roomTypeBySeats;
        this.roomClass = roomClass;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.claimId = claimId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public LocalDate getConfirmRequestDate() {
        return confirmRequestDate;
    }

    public LocalDate getConfirmRequestDueDate() {
        return confirmRequestDueDate;
    }

    public String getRoomTypeBySeats() {
        return roomTypeBySeats;
    }

    public String getRoomClass() {
        return roomClass;
    }

    public LocalDate getCheckinDate() {
        return checkinDate;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public int getClaimId() {
        return claimId;
    }

    public String getStatus() {
        return status;
    }
}
