package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.dto.ClaimDto;
import com.epam.javacourse.hotelapp.dto.InvoiceDto;
import com.epam.javacourse.hotelapp.dto.RoomDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.ConfirmationRequest;
import com.epam.javacourse.hotelapp.model.Invoice;
import com.epam.javacourse.hotelapp.model.Room;
import com.epam.javacourse.hotelapp.repository.CustomizedRoomRepository;
import com.epam.javacourse.hotelapp.repository.RoomRepository;
import com.epam.javacourse.hotelapp.service.interfaces.IRoomService;
import com.epam.javacourse.hotelapp.utils.mappers.ConfirmationRequestMapper;
import com.epam.javacourse.hotelapp.utils.mappers.InvoiceMapper;
import com.epam.javacourse.hotelapp.utils.mappers.RoomMapper;
import com.epam.javacourse.hotelapp.utils.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
    public List<RoomDto> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        List<RoomDto> roomsDto = new ArrayList<>();
        rooms.forEach(x -> roomsDto.add(RoomMapper.mapToDto(x)));
        return roomsDto;
    }

    @Override
    public RoomDto getRoomById(int id) {

        Optional<Room> optionalRoom = roomRepository.findById(id);
        Room room;
        if (optionalRoom.isEmpty()) {
            logger.error("Can't get room with id = {}", id);
            throw new NoSuchElementException("Not found room with id = " + id);
        } else {
            room = optionalRoom.get();
        }
        return RoomMapper.mapToDto(room);
    }

    @Override
    public void deleteRoomById(int id) {
        this.roomRepository.deleteById(id);
    }

    @Override
    public RoomDto chooseSuitableRoomForRequest(ClaimDto claimDto, List<RoomDto> freeRooms) throws AppException {

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
            throw new AppException(errorMessage, exception);
        }
        return suitableRoom;
    }


    @Override
    public List<Room> getFreeRoomsForPeriod(LocalDate checkin, LocalDate checkout) throws AppException {
        try {
            return customizedRoomRepository.findAvailableRoomsForPeriod(checkin, checkout);
        } catch (DBException exception) {
            throw new AppException("Can't retrieve free rooms for the specified period from " + checkin + " to " + checkout, exception);
        }
    }

    @Override
    public int getRoomsNumberForPeriod(LocalDate checkin, LocalDate checkout) throws AppException {
        try {
            Validator.ensureDatesAreValid(checkin, checkout);
            return customizedRoomRepository.findAvailableRoomsForPeriod(checkin, checkout).size();
        } catch (DBException exception) {
            throw new AppException("Can't retrieve free rooms number for the specified period from " + checkin + " to " + checkout, exception);
        }
    }

    @Override
    public List<Room> getRoomsForPeriod(LocalDate checkin, LocalDate checkout, int pageSize, int page,
                                        String sortBy, String sortType, String sortSeats, String status) throws AppException {
        try {
            return customizedRoomRepository.findRoomsToBook(checkin, checkout, pageSize, page, sortBy, sortType, sortSeats, status);
        } catch (DBException exception) {
            throw new AppException("Can't retrieve rooms for the specified period from " + checkin + " to " + checkout, exception);
        }

    }


    @Override
    public List<Integer> getRoomsNumbers() throws AppException {
        try {
            return roomRepository.findAllRoomNumbers();
        } catch (Exception exception) {
            throw new AppException("Can't get all rooms' numbers");
        }
    }


//    public long getTotalRooms() {
//        logger.info("Finding the total count of rooms from the DB.");
//        return roomRepository.count();
//    }


}
