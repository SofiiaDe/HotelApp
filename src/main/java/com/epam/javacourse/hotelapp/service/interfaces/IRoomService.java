package com.epam.javacourse.hotelapp.service.interfaces;

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
    void saveRoom(Room room);
    void deleteRoomById(int id);
    List<Room> findPaginated(int pageNo, int pageSize, String sortBy, String sortType, String roomStatus, String roomSeats);
    List<Room> getAvailableRoomsForPeriod(LocalDate checkin, LocalDate checkout);
    int getRoomsNumberForPeriod(LocalDate checkinDate, LocalDate checkoutDate) throws AppException;
    List<Room> getAvailablePageableRoomsForPeriod(LocalDate checkin, LocalDate checkout, int pageSize, int page, Sort sortType, String sortSeats);
//    Page<Room> findPaginated(int pageNo, int pageSize, String sortBy, String sortType, String roomStatus, String roomSeats);
}
