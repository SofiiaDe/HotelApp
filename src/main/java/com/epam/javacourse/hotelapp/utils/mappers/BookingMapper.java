package com.epam.javacourse.hotelapp.utils.mappers;

import com.epam.javacourse.hotelapp.dto.BookingDto;
import com.epam.javacourse.hotelapp.model.Booking;


public class BookingMapper {

    public static BookingDto mapToDto(Booking booking){
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUserId().getId());
        dto.setCheckinDate(booking.getCheckinDate());
        dto.setCheckoutDate(booking.getCheckoutDate());
        dto.setRoomId(booking.getRoomId());
        dto.setClaimId(booking.getClaimId());
        return dto;
    }


    public static Booking mapFromDto(BookingDto dto){
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setUserId(dto.getUser());
        booking.setCheckinDate(dto.getCheckinDate());
        booking.setCheckoutDate(dto.getCheckoutDate());
        booking.setRoomId(dto.getRoomId());
        booking.setClaimId(dto.getClaimId());
        return booking;
    }
}
