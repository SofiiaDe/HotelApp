package com.epam.javacourse.hotelapp.repository;

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

    @Transactional
    @Override
    public List<Room> findRoomsToBook(LocalDate checkin, LocalDate checkout, int pageSize, int page, String sortBy, String sortType, String sortSeats, String roomStatus) {
        List<Room> paginatedResult = new ArrayList<>();
        StoredProcedureQuery spQuery;

        try {
            if (roomStatus == null) {
                spQuery = this.entityManager.createNamedStoredProcedureQuery("getAvailableRooms");
            } else {
                switch (roomStatus) {
                    case "reserved":
                        spQuery = this.entityManager.createNamedStoredProcedureQuery("getReservedRooms");
                        break;
                    case "booked":
                        spQuery = this.entityManager.createNamedStoredProcedureQuery("getBookedRooms");
                        break;
                    case "unavailable":
                        spQuery = this.entityManager.createNamedStoredProcedureQuery("getUnavailableRooms");
                        break;

                    default:
                        spQuery = this.entityManager.createNamedStoredProcedureQuery("getAvailableRooms");
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
            exception.printStackTrace();
        }

        return paginatedResult;
    }

    @Override
    public List<Room> findAvailableRooms(LocalDate checkin, LocalDate checkout) {

        TypedQuery<Room> query = entityManager.createQuery(HQL_AVAILABLE_ROOMS, Room.class);
        List<Room> availableRooms = query.getResultList();

        List<Integer> test = getOccupiedRoomsIds(checkin, checkout);

        return availableRooms.stream()
                .filter(room -> !test.contains(room.getId())).collect(Collectors.toList());
    }

    private List<Integer> getOccupiedRoomsIds(LocalDate checkin, LocalDate checkout) {
        String hql = "SELECT b FROM Booking b LEFT JOIN Invoice i ON i.bookingId.id = b.id " +
                "WHERE (b.checkinDate <= :checkin AND b.checkoutDate >= :checkout) AND i.invoiceStatus != 'cancelled'";
        TypedQuery<Booking> query = entityManager.createQuery(hql, Booking.class);
        query.setParameter("checkin", checkin);
        query.setParameter("checkout", checkout);
        List<Booking> validBookings = query.getResultList();
        return validBookings.stream()
                .map(Booking::getRoomId)
                .distinct()
                .collect(Collectors.toList());
    }

}



