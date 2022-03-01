package com.epam.javacourse.hotelapp.dto;

import java.time.LocalDate;

public class ConfirmationRequestManagerDto {

    private int id;
    private String bookedByUser;
    private String bookedByUserEmail;
    private int claimId;
    private int roomId;
    private LocalDate confirmRequestDate;
    private String status;

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
