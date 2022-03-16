package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.dto.ClaimDto;
import com.epam.javacourse.hotelapp.dto.RoomDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.Room;
import com.epam.javacourse.hotelapp.repository.CustomizedRoomRepository;
import com.epam.javacourse.hotelapp.repository.RoomRepository;
import com.epam.javacourse.hotelapp.service.interfaces.IRoomService;
import com.epam.javacourse.hotelapp.utils.mappers.RoomMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RoomServiceImpl implements IRoomService {

    private static final Logger logger = LogManager.getLogger(RoomServiceImpl.class);

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    CustomizedRoomRepository customizedRoomRepository;

    @Override
    public void createRoom(RoomDto room) {
        roomRepository.save(RoomMapper.mapFromDto(room));
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomById(int id) throws AppException {
        Optional<Room> optional = roomRepository.findById(id);
        Room room;
        if (optional.isPresent()) {
            room = optional.get();
        } else {
            throw new RuntimeException("Room not found for id :: " + id);
        }
        return room;
    }

    @Override
    public void deleteRoomById(int id) {
        this.roomRepository.deleteById(id);
    }

    @Override
    public RoomDto chooseSuitableRoomForRequest(ClaimDto claimDto, List<RoomDto> freeRooms) {

        RoomDto suitableRoom = null;

        try {
            for (RoomDto freeRoom : freeRooms) {
                if ((claimDto.getRoomSeats().equals(freeRoom.getRoomSeats()))
                        && (claimDto.getRoomClass().equals(freeRoom.getRoomClass()))) {
                    return freeRoom;
                } else if (claimDto.getRoomSeats().equals(freeRoom.getRoomSeats())) {
                    return freeRoom;
                } else if (claimDto.getRoomClass().equals(freeRoom.getRoomClass())) {
                    return freeRoom;
                } else {
                    suitableRoom = freeRoom;
                }
            }
        } catch (Exception exception) {
            String errorMessage = "Can't select suitable room to make confirmation request for application with id=" + claimDto.getId();
            logger.error(errorMessage, exception);
        }
        return suitableRoom;
    }


    @Override
    public List<Room> getFreeRoomsForPeriod(LocalDate checkin, LocalDate checkout) throws AppException {
        return customizedRoomRepository.findAvailableRooms(checkin, checkout);
    }

    @Override
    public int getRoomsNumberForPeriod(LocalDate checkinDate, LocalDate checkoutDate) throws AppException {

        ensureDatesAreValid(checkinDate, checkoutDate);
        return customizedRoomRepository.findAvailableRooms(checkinDate, checkoutDate).size();
    }

    @Override
    public List<Room> getRoomsForPeriod(LocalDate checkin, LocalDate checkout, int pageSize, int page, String sortBy, String sortType, String sortSeats, String status) {
        return customizedRoomRepository.findRoomsToBook(checkin, checkout, pageSize, page, sortBy, sortType, sortSeats, status);
    }


    /**
     * Validates if checkin date is not after checkout date or checkout date is equal to checkin date
     *
     * @throws AppException in case of at least one of dates is incorrect
     */
    private void ensureDatesAreValid(LocalDate checkinDate, LocalDate checkoutDate) throws AppException {
        if (checkinDate.isAfter(checkoutDate) || checkoutDate.isEqual(checkinDate)) {
            throw new AppException("Check-in and check-out dates are overlapping or equal");

        }
    }

    @Override
    public List<Integer> getRoomsNumbers() throws AppException {
        try {
            return roomRepository.findAllRoomNumbers();
        } catch (DBException exception) {
            throw new AppException("Can't get all rooms' numbers");
        }
    }


//    public long getTotalRooms() {
//        logger.info("Finding the total count of rooms from the DB.");
//        return roomRepository.count();
//    }


}
