package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.Booking;
import com.epam.javacourse.hotelapp.model.Room;
import org.hibernate.annotations.SortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.epam.javacourse.hotelapp.utils.Constants.*;


@Component
public class CustomizedRoomRepositoryImpl implements CustomizedRoomRepository {

    @Autowired
    @Lazy
    RoomRepository roomRepository;


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Room> findAvailableRoomsForPeriod(LocalDate checkin, LocalDate checkout) throws DBException {
        List<Room> availableRooms;
        StoredProcedureQuery spQuery;

        try {
            spQuery = this.entityManager.createNamedStoredProcedureQuery(PROCEDURE_AVAILABLE_ROOMS);

            spQuery.setParameter("checkin_date", checkin);
            spQuery.setParameter("checkout_date", checkout);
            spQuery.setParameter("room_seats", null);

            spQuery.execute();
            availableRooms = (List<Room>) spQuery.getResultList();

            entityManager.close();

        } catch (Exception exception) {
            throw new DBException("Can't find available rooms for defined period", exception);
        }
        return availableRooms;
    }

    @Transactional
    @Override
    public List<Room> findRoomsToBook(LocalDate checkin, LocalDate checkout, int pageSize, int page,
                                      String sortBy, String sortType, String sortSeats, String roomStatus) throws DBException {
//    public List<Room> findRoomsToBook(LocalDate checkin, LocalDate checkout, int pageSize, int page, Sort sortType, String sortSeats, String roomStatus) {

        List<Room> paginatedResult = new ArrayList<>();
        StoredProcedureQuery spQuery;

        try {
            if (roomStatus == null) {
                spQuery = this.entityManager.createNamedStoredProcedureQuery(PROCEDURE_AVAILABLE_ROOMS);
            } else {
                switch (roomStatus) {
                    case "reserved":
                        spQuery = this.entityManager.createNamedStoredProcedureQuery(PROCEDURE_RESERVED_ROOMS);
                        break;
                    case "booked":
                        spQuery = this.entityManager.createNamedStoredProcedureQuery(PROCEDURE_BOOKED_ROOMS);
                        break;
                    case "unavailable":
                        spQuery = this.entityManager.createNamedStoredProcedureQuery(PROCEDURE_UNAVAILABLE_ROOMS);
                        break;

                    default:
                        spQuery = this.entityManager.createNamedStoredProcedureQuery(PROCEDURE_AVAILABLE_ROOMS);
                }
            }
            spQuery.setParameter("checkin_date", checkin);
            spQuery.setParameter("checkout_date", checkout);

            if (sortSeats != null && !sortSeats.isEmpty()) {
                spQuery.setParameter("room_seats", sortSeats);
            } else {
                spQuery.setParameter("room_seats", null);
            }

//            if (sortType.isSorted()) {
//
//                var sortParameters = sortType.toList();
//
//                if (!sortType.isEmpty()) {
//                    for (var s : sortParameters) {
//                        spQuery.setParameter("sortBy", s.getProperty());
//                        spQuery.setParameter("sortType", s.getDirection().toString().toLowerCase());
//                    }
//                }
//            }

            if (sortBy != null && !sortBy.isEmpty()) {
                spQuery.setParameter("sortBy", sortBy);
            }
            if (sortType != null && !sortType.isEmpty()) {
                spQuery.setParameter("sortType", sortType);
            }

            spQuery.execute();
            List<Room> result = (List<Room>) spQuery.getResultList();

            entityManager.close();

            paginatedResult = result.stream()
                    .skip((long) pageSize * (page - 1)).limit(pageSize).collect(Collectors.toList());

        } catch (Exception exception) {
            throw new DBException("Can't find rooms to show in booking page", exception);
        }

        return paginatedResult;
    }

}



