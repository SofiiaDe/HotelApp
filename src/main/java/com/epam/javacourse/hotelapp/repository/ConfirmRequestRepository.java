package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.model.ConfirmationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmRequestRepository extends JpaRepository<ConfirmationRequest, Integer> {
}
