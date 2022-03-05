package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.model.Room;

import java.time.LocalDate;
import java.util.List;

public interface CustomizedRoomRepository {

    List<Room> findAvailableRooms(LocalDate checkin, LocalDate checkout);
    List<Room> findAvailablePageableRooms(LocalDate checkin, LocalDate checkout, int pageSize, int page);
}
