package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.Claim;
import com.epam.javacourse.hotelapp.model.ConfirmationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ConfirmRequestRepository extends JpaRepository<ConfirmationRequest, Integer> {

    @Query("SELECT c FROM ConfirmationRequest c WHERE c.userId.id = :id")
    List<ConfirmationRequest> findConfirmationRequestsByUserId(@Param("id") int userId);

    @Modifying
    @Query("UPDATE ConfirmationRequest c SET c.status = :status WHERE c.id = :id")
    void updateConfirmRequestStatus(@Param("status") String status, @Param("id") Integer confirmRequestId);

}
