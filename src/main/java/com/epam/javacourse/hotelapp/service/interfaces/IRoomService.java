package com.epam.javacourse.hotelapp.service.interfaces;

import com.epam.javacourse.hotelapp.dto.ClaimDto;
import com.epam.javacourse.hotelapp.dto.RoomDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.Room;

import java.time.LocalDate;
import java.util.List;

public interface IRoomService {

    RoomDto getRoomById(int roomId) throws AppException;

    List<RoomDto> getAllRooms();

    void createRoom(RoomDto room);

    void deleteRoomById(int id);

    RoomDto chooseSuitableRoomForRequest(ClaimDto claimDto, List<RoomDto> freeRooms) throws AppException;

    List<Integer> getRoomsNumbers() throws AppException;

    List<Room> getFreeRoomsForPeriod(LocalDate checkin, LocalDate checkout) throws AppException;

    int getRoomsNumberForPeriod(LocalDate checkinDate, LocalDate checkoutDate) throws AppException;

    List<Room> getRoomsForPeriod(LocalDate checkin, LocalDate checkout, int pageSize, int page,
                                 String sortBy, String sortType, String sortSeats, String status) throws AppException;
}
