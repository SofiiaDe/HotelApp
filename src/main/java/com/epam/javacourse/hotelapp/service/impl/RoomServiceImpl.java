package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.Room;
import com.epam.javacourse.hotelapp.repository.CustomizedRoomRepository;
import com.epam.javacourse.hotelapp.repository.RoomRepository;
import com.epam.javacourse.hotelapp.service.interfaces.IRoomService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

//    public Optional<Room> getRoomById(int roomId) throws AppException {
//        return roomRepository.findById(roomId);
//    }

    public void saveRoom(final Room room) {
        roomRepository.save(room);
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

//    int pageNumber = 1;
//    int pageSize = 5;
//    Pageable pageable = PageRequest.of(pageNumber, pageSize);

//    Page<Room> page = roomRepository.findAll(pageable);
//    List<Room> listRooms = page.getContent();

//    long totalItems = page.getTotalElements();
//    int totalPages = page.getTotalPages();

    @Override
    public List<Room> getAvailableRoomsForPeriod(LocalDate checkin, LocalDate checkout) {
        return customizedRoomRepository.findAvailableRooms(checkin, checkout);
    }

    @Override
    public int getRoomsNumberForPeriod(LocalDate checkinDate, LocalDate checkoutDate) throws AppException {

        ensureDatesAreValid(checkinDate, checkoutDate);
        return customizedRoomRepository.findAvailableRooms(checkinDate, checkoutDate).size();
    }

    @Override
    public List<Room> getAvailablePageableRoomsForPeriod(LocalDate checkin, LocalDate checkout, int pageSize, int page) {
        return customizedRoomRepository.findAvailablePageableRooms(checkin, checkout, pageSize, page);
    }

    /**
     * validates if checkin date is not after checkout date or checkout date is equal to checkin date
     *
     * @param checkinDate
     * @param checkoutDate
     * @throws AppException in case of at least one of dates is incorrect
     */
    private void ensureDatesAreValid(LocalDate checkinDate, LocalDate checkoutDate) throws AppException {
        if (checkinDate.isAfter(checkoutDate) || checkoutDate.isEqual(checkinDate)) {
            throw new AppException("Check-in and check-out dates are overlapping or equal");

        }
    }

    @Override
    public List<Room> findPaginated(int pageNo, int pageSize, String sortBy, String sortType, String roomStatus, String roomSeats) {
        logger.info("Fetching the paginated rooms from the DB.");
        Sort sort = Objects.equals(sortBy, "class") ? Sort.by("roomClass") : Sort.by("price");

        Sort sortOrder = Objects.equals(sortType, "asc") ? sort.ascending() : sort.descending();

        sort.and(sortOrder);
        Sort roomStatusSort;

//        List<Room> result = customizedRoomRepository.findAvailablePageableRooms()

//        if(roomStatus == "available") {
        //roomStatusSort = Sort.by().
//            Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
//            this.roomRepository.findByRoomClassEquals(pageable, "`");
//        }

//        sort = sort.and(roomStatusSort);


//        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
//                Sort.by(sortField).descending();
//
//        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        //this.roomRepository.findBy()
//        try{
//            return this.roomRepository.findAll(pageable);
//            Page<Room> result = (Page<Room>)
//        var data = this.customizedRoomRepository.findAvailablePageableRooms();

//            return null;
//
//        }
//        catch (Exception exception)
//        {
//            var i =0;
//        }
        return null;
    }



//    public long getTotalRooms() {
//        logger.info("Finding the total count of rooms from the DB.");
//        return roomRepository.count();
//    }


}
