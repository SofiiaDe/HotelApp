package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.Room;

import java.time.LocalDate;
import java.util.List;

public interface CustomizedRoomRepository {

    List<Room> findAvailableRoomsForPeriod(LocalDate checkin, LocalDate checkout) throws DBException;

    List<Room> findRoomsToBook(LocalDate checkin, LocalDate checkout, int pageSize, int page,
                               String sortBy, String sortType, String sortSeats, String status) throws DBException;
//    List<Room> findRoomsToBook(LocalDate checkin, LocalDate checkout, int pageSize, int page, Sort sortType, String sortSeats, String status);

 }
