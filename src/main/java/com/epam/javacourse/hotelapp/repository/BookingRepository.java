package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT b FROM Booking b WHERE b.userId.id = :id")
    List<Booking> findBookingsByUserId(@Param("id") int userId);

    @Modifying
    @Query("UPDATE Booking b SET b.status = :status where b.id = :id")
    void updateBookingStatus(@Param("status") boolean status, @Param("id") Integer bookingId);
}
