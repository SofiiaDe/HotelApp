package com.epam.javacourse.hotelapp.service.interfaces;

import com.epam.javacourse.hotelapp.dto.BookingClientDto;
import com.epam.javacourse.hotelapp.dto.BookingDto;
import com.epam.javacourse.hotelapp.dto.BookingManagerDto;
import com.epam.javacourse.hotelapp.dto.ClaimDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.utils.enums.BookingStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IBookingService {

    @Transactional
    void createBooking(BookingDto bookingDto);

    List<BookingDto> getBookingsByUserId(int userId) throws AppException;

    void cancelUnpaidBookings() throws AppException;

    List<BookingManagerDto> getAllDetailedBookings(BookingStatus bookingStatus) throws AppException;

    List<BookingClientDto> getUserDetailedBookings(int userID) throws AppException;

}
