package com.epam.javacourse.hotelapp.dto;

import java.time.LocalDate;

/**
 * DTO to show all confirmation requests in Manager's account
 */
public class ConfirmationRequestManagerDto {

    private final int id;
    private final String bookedByUser;
    private final String bookedByUserEmail;
    private final int claimId;
    private final int roomId;
    private final LocalDate confirmRequestDate;
    private final String status;

    public ConfirmationRequestManagerDto(int id, String bookedByUser, String bookedByUserEmail,
                                         int claimId, int roomId, LocalDate confirmRequestDate, String status) {
        this.id = id;
        this.bookedByUser = bookedByUser;
        this.bookedByUserEmail = bookedByUserEmail;
        this.claimId = claimId;
        this.roomId = roomId;
        this.confirmRequestDate = confirmRequestDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getBookedByUser() {
        return bookedByUser;
    }

    public String getBookedByUserEmail() {
        return bookedByUserEmail;
    }

    public int getClaimId() {
        return claimId;
    }

    public int getRoomId() {
        return roomId;
    }

    public LocalDate getConfirmRequestDate() {
        return confirmRequestDate;
    }

    public String getStatus() {
        return status;
    }
}
