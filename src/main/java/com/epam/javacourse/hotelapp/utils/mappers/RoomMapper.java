package com.epam.javacourse.hotelapp.utils.mappers;

import com.epam.javacourse.hotelapp.dto.RoomDto;
import com.epam.javacourse.hotelapp.model.Room;

public class RoomMapper {

    public static RoomDto mapToDto(Room room) {
        RoomDto dto = new RoomDto();
        dto.setId(room.getId());
        dto.setPrice(room.getPrice());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setRoomSeats(room.getRoomSeats());
        dto.setRoomClass(room.getRoomClass());
        dto.setRoomStatus(room.getRoomStatus());
        return dto;
    }

    public static Room mapFromDto(RoomDto dto){
        Room room = new Room();
        room.setId(dto.getId());
        room.setPrice(dto.getPrice());
        room.setRoomNumber(dto.getRoomNumber());
        room.setRoomSeats(dto.getRoomSeats());
        room.setRoomClass(dto.getRoomClass());
        room.setRoomStatus(dto.getRoomStatus());
        return room;
    }
}
