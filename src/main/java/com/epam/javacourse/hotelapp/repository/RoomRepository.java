package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
}
