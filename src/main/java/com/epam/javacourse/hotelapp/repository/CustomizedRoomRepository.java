package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.Room;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface
 */
public interface CustomizedRoomRepository {

    /**
     * Find all available rooms for defined period
     * @throws DBException
     */
    List<Room> findAvailableRoomsForPeriod(LocalDate checkin, LocalDate checkout) throws DBException;

    /**
     * Find all available rooms for defined period considering pagination and sorting
     * @throws DBException when exception is thrown while accessing DB
     */
    List<Room> findRoomsToBook(LocalDate checkin, LocalDate checkout, int pageSize, int page,
                               String sortBy, String sortType, String sortSeats, String status) throws DBException;

 }
