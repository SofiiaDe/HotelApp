package com.epam.javacourse.hotelapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "confirmation_requests")
public class ConfirmationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private int id;

    @JoinColumn(name = "user_id")
    private int userId;

    @Column(name = "application_id")
    private int applicationId;

    @Column(name = "room_id")
    private int roomId;

    @Column(name = "confirm_request_date")
    private LocalDate confirmRequestDate;

    private LocalDate confirmRequestDueDate;

    @Column(name = "status")
    private String status;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getConfirmRequestDueDate() {
        return confirmRequestDueDate;
    }

    public void setConfirmRequestDueDate(LocalDate confirmRequestDueDate) {
        this.confirmRequestDueDate = confirmRequestDueDate;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

enum ConfirmRequestStatus{
    NEW,
    CONFIRMED,
    CANCELLED,
}