package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.Claim;
import com.epam.javacourse.hotelapp.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
//    Page<Room> findByRoomClassEquals(PageRequest pageRequest, String roomsClass);


    @Query("SELECT r.roomNumber FROM Room r ORDER BY r.roomNumber DESC")
    List<Integer> findAllRoomNumbers();

}
