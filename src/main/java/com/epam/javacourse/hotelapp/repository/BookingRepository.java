package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.model.Booking;
import com.epam.javacourse.hotelapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
