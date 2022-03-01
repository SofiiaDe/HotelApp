package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.ConfirmationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfirmRequestRepository extends JpaRepository<ConfirmationRequest, Integer> {

    @Query("SELECT c FROM ConfirmationRequest c WHERE c.userId.id = :id")
    List<ConfirmationRequest> findConfirmationRequestsByUserId(@Param("id") int userId) throws DBException;

}
