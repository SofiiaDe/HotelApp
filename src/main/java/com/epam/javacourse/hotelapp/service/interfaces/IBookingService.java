package com.epam.javacourse.hotelapp.service.interfaces;

import com.epam.javacourse.hotelapp.dto.BookingClientDto;
import com.epam.javacourse.hotelapp.dto.BookingDto;
import com.epam.javacourse.hotelapp.dto.BookingManagerDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.utils.enums.BookingStatus;

import java.util.List;

public interface IBookingService {

//    boolean create(Booking booking) throws AppException;
//
    List<BookingDto> getBookingsByUserId(int userId) throws AppException;
//
//    List<Booking> getAllBookings() throws AppException;
//
//    Booking getBookingById(int id) throws AppException;

    List<BookingManagerDto> getAllDetailedBookings(BookingStatus bookingStatus) throws AppException;

//    void deleteBookingById(int id) throws AppException;

    List<BookingClientDto> getUserDetailedBookings(int userID) throws AppException;

}
