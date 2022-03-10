package com.epam.javacourse.hotelapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "confirmation_requests")
public class ConfirmationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private int id;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User userId;

    @OneToOne
    @JoinColumn(name = "claim_id")
    private Claim claimId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id")
    private Room roomId;

    @Column(name = "confirm_request_date")
    private LocalDate confirmRequestDate;

//    private LocalDate confirmRequestDueDate;

    @Column(name = "status")
    private String status;

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

//    public LocalDate getConfirmRequestDueDate() {
//        return confirmRequestDueDate;
//    }
//
//    public void setConfirmRequestDueDate(LocalDate confirmRequestDueDate) {
//        this.confirmRequestDueDate = confirmRequestDueDate;
//    }

    public Claim getClaimId() {
        return claimId;
    }

    public void setClaimId(Claim claimId) {
        this.claimId = claimId;
    }

    public Room getRoomId() {
        return roomId;
    }

    public void setRoomId(Room roomId) {
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

