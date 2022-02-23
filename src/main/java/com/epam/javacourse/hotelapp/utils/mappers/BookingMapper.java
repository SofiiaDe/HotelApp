package com.epam.javacourse.hotelapp.utils.mappers;

import com.epam.javacourse.hotelapp.dto.ApplicationDto;
import com.epam.javacourse.hotelapp.dto.BookingDto;
import com.epam.javacourse.hotelapp.model.Application;
import com.epam.javacourse.hotelapp.model.Booking;

import java.time.LocalDate;

public class BookingMapper implements DtoMapper<BookingDto, Booking>{

    @Override
    public BookingDto mapToDto(Booking booking){
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUserId().getId());
        dto.setCheckinDate(booking.getCheckinDate());
        dto.setCheckoutDate(booking.getCheckoutDate());
        return dto;
    }

    @Override
    public Booking mapFromDto(BookingDto dto){
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setCheckinDate(dto.getCheckinDate());
        booking.setCheckoutDate(dto.getCheckoutDate());

        return booking;
    }
}

