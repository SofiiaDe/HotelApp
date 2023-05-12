package com.epam.javacourse.hotelapp.dto;

import java.time.LocalDate;

/**
 * DTO to show list of Client's confirmation requests in Client's account
 */
public class ConfirmationRequestClientDto {

    private final int id;
    private final LocalDate confirmRequestDate;
    private final LocalDate confirmRequestDueDate;
    private final String roomSeats;
    private final String roomClass;
    private final LocalDate checkinDate;
    private final LocalDate checkoutDate;
    private final int claimId;
    private final String status;

    public ConfirmationRequestClientDto(int id, LocalDate confirmRequestDate,
                                        LocalDate confirmRequestDueDate,
                                        String roomSeats, String roomClass,
                                        LocalDate checkinDate, LocalDate checkoutDate,
                                        int claimId, String status) {
        this.id = id;
        this.confirmRequestDate = confirmRequestDate;
        this.confirmRequestDueDate = confirmRequestDueDate;
        this.roomSeats = roomSeats;
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

    public String getRoomSeats() {
        return roomSeats;
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
