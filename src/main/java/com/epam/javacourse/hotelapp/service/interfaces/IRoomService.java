package com.epam.javacourse.hotelapp.service.interfaces;

import com.epam.javacourse.hotelapp.dto.ClaimDto;
import com.epam.javacourse.hotelapp.dto.RoomDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IRoomService {

    //    Optional<Room> getRoomById(int roomId) throws AppException;
    Room getRoomById(int roomId) throws AppException;

    List<Room> getAllRooms();

    void createRoom(RoomDto room);

    void deleteRoomById(int id);

    RoomDto chooseSuitableRoomForRequest(ClaimDto claimDto, List<RoomDto> freeRooms);

    List<Integer> getRoomsNumbers() throws AppException;

    List<Room> getFreeRoomsForPeriod(LocalDate checkin, LocalDate checkout) throws AppException;

    int getRoomsNumberForPeriod(LocalDate checkinDate, LocalDate checkoutDate) throws AppException;

//    Page<Room> findPaginated(int pageNo, int pageSize, String sortBy, String sortType, String roomStatus, String roomSeats);

    List<Room> getRoomsForPeriod(LocalDate checkin, LocalDate checkout, int pageSize, int page, String sortBy, String sortType, String sortSeats, String status);
//List<Room> getRoomsForPeriod(LocalDate checkin, LocalDate checkout, int pageSize, int page, Sort sortType, String sortSeats, String status);

}
