package com.epam.javacourse.hotelapp.service.interfaces;

import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.Room;

import java.util.Optional;

public interface IRoomService {

    Optional<Room> getRoomById(int roomId) throws AppException;
}
