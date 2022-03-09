package com.epam.javacourse.hotelapp.dto;

import com.epam.javacourse.hotelapp.model.Claim;
import com.epam.javacourse.hotelapp.model.Room;
import com.epam.javacourse.hotelapp.model.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import java.time.LocalDate;

public class ConfirmationRequestDto {

    private int id;
    private int userId;
    private int claimId;
    private int roomId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate confirmRequestDate;

    private String status;

    private LocalDate confirmRequestDueDate;
    private User user;
    private Claim claim;
    private Room room;

    public ConfirmationRequestDto() {
    }

    public ConfirmationRequestDto(int id, int userId, int claimId, int roomId,
                                  LocalDate confirmRequestDate, String status) {
        this.id = id;
        this.userId = userId;
        this.claimId = claimId;
        this.roomId = roomId;
        this.confirmRequestDate = confirmRequestDate;
        this.status = status;
    }

    public int getId() {
        return id;
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

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public LocalDate getConfirmRequestDate() {
        return confirmRequestDate;
    }

    public void setConfirmRequestDate(LocalDate confirmRequestDate) {
        this.confirmRequestDate = confirmRequestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getConfirmRequestDueDate() {
        return confirmRequestDueDate;
    }

    public void setConfirmRequestDueDate(LocalDate confirmRequestDueDate) {
        this.confirmRequestDueDate = confirmRequestDueDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
