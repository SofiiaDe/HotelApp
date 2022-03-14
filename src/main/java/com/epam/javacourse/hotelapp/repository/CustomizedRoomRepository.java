package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.model.Room;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

public interface CustomizedRoomRepository {

    List<Room> findAvailableRooms(LocalDate checkin, LocalDate checkout);

    List<Room> findRoomsToBook(LocalDate checkin, LocalDate checkout, int pageSize, int page, String sortBy, String sortType, String sortSeats, String status);
 }
