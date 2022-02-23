package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.Room;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.RoomRepository;
import com.epam.javacourse.hotelapp.service.interfaces.IRoomService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomServiceImpl implements IRoomService {

    RoomRepository roomRepository;

    public Optional<Room> getRoomById(int roomId) throws AppException {
        return roomRepository.findById(roomId);
    }

}
