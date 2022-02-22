package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.model.Application;
import com.epam.javacourse.hotelapp.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
}
