package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.model.Booking;
import com.epam.javacourse.hotelapp.model.Room;
import org.hibernate.annotations.SortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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

    public static final String SQL_GET_AVAILABLE_ROOMS = "SELECT * FROM rooms r LEFT OUTER JOIN (SELECT DISTINCT(room_id) " +
            "FROM bookings b LEFT JOIN invoices i ON i.booking_id = b.id WHERE (b.checkin_date <= ? AND b.checkout_date >= ?) " +
            "AND i.status != 'cancelled') q ON q.room_id = r.id WHERE q.room_id IS null AND room_status = 'available' ";

    public static final String SQL_GET_ROOMS_BASIC_QUERY = "SELECT ?0? FROM rooms r LEFT OUTER JOIN (SELECT DISTINCT(room_id) " +
            "FROM bookings b LEFT JOIN invoices i ON i.booking_id = b.id WHERE (b.checkin_date <= ? AND b.checkout_date >= ?) " +
            "AND i.status ?1?) q ON q.room_id = r.id WHERE q.room_id IS ?2? null ";

//     case RESERVED:
//    result = result.replace("?1?", "= 'new' ");
//    result = result.replace("?2?", "not");

//            case BOOKED:
//    result = result.replace("?1?", "= 'paid' ");
//    result = result.replace("?2?", "not");

//            case UNAVAILABLE:
//    result = result.replace("?1?", "!= 'cancelled' ");
//    result += "and room_status = 'unavailable' ";
//    result = result.replace("?2?", "")
//
//    default:
//    result = result.replace("?1?", "!= 'cancelled'");
//    result += "and room_status = 'available' ";
//    result = result.replace("?2?", "");

    @PersistenceContext
    private EntityManager entityManager;

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

    @Override
    public List<Room> findAvailableRooms(LocalDate checkin, LocalDate checkout) {

        TypedQuery<Room> query = entityManager.createQuery(HQL_AVAILABLE_ROOMS, Room.class);
        List<Room> availableRooms = query.getResultList();

        List<Integer> test = getOccupiedRoomsIds(checkin, checkout);

        return availableRooms.stream()
                .filter(room -> !test.contains(room.getId())).collect(Collectors.toList());
    }

    @Override
    public List<Room> findPageableRooms(LocalDate checkin, LocalDate checkout, int pageSize, int page, Sort sortType, String sortSeats, String sortStatus) {

        if (sortSeats != null && !sortSeats.isEmpty()) {
            HQL_AVAILABLE_ROOMS += " AND room_seats = '" + sortSeats.toLowerCase() + "'";
        }

        if (sortType.isSorted()) {

            if (!sortType.isEmpty()) {
                HQL_AVAILABLE_ROOMS += " ORDER BY ";
            }

            var sortParameters = sortType.toList();
            var lastElement = sortParameters.get(sortParameters.size() - 1);
            for (var s : sortParameters) {
                HQL_AVAILABLE_ROOMS += s.getProperty() + ' ' + s.getDirection();
                if (s != lastElement) {
                    HQL_AVAILABLE_ROOMS += ",";
                }
            }
        }


        TypedQuery<Room> query = entityManager.createQuery(HQL_AVAILABLE_ROOMS, Room.class);
        List<Room> availableRooms = query.getResultList();

        List<Integer> test = getOccupiedRoomsIds(checkin, checkout);

        var result = availableRooms.stream()
                .filter(room -> !test.contains(room.getId())).skip(pageSize * (page - 1)).limit(pageSize).collect(Collectors.toList());

        return result;
    }

    @Override
    public List<Room> findPageableRoomsSortedByStatus(LocalDate checkin, LocalDate checkout, int pageSize, int page, Sort sortType, String sortSeats, String sortStatus) {

        String hql = "SELECT r FROM Room r WHERE r.roomStatus = 'available'";

        if (sortSeats != null && !sortSeats.isEmpty()) {
            hql += " AND room_seats = '" + sortSeats.toLowerCase() + "'";
        }

        if (sortType.isSorted()) {

            if (!sortType.isEmpty()) {
                hql += " ORDER BY ";
            }

            var sortParameters = sortType.toList();
            var lastElement = sortParameters.get(sortParameters.size() - 1);
            for (var s : sortParameters) {
                hql += s.getProperty() + ' ' + s.getDirection();
                if (s != lastElement) {
                    hql += ",";
                }
            }
        }


        TypedQuery<Room> query = entityManager.createQuery(hql, Room.class);
        List<Room> availableRooms = query.getResultList();

        List<Integer> test = getOccupiedRoomsIds(checkin, checkout);

        var result = availableRooms.stream()
                .filter(room -> !test.contains(room.getId())).skip(pageSize * (page - 1)).limit(pageSize).collect(Collectors.toList());

        if (sortStatus != null) {

            switch (sortStatus) {

                case "reserved":
                    List<Integer> reservedRoomsIds = getRoomsIds(checkin, checkout, "new");
                    List<Room> reservedRooms = roomRepository.findAllById(reservedRoomsIds);
                    result = result.stream()
                            .filter(reservedRooms::contains)
                            .collect(Collectors.toList());
                    break;
                case "booked":
                    List<Integer> bookedRoomsIds = getRoomsIds(checkin, checkout, "paid");
                    List<Room> bookedRoomIds = roomRepository.findAllById(bookedRoomsIds);
                    break;
                case "unavailable":
                    List<Integer> unavailableRoomsIds = getOccupiedRoomsIds(checkin, checkout);
                    List<Room> unavailableRooms = roomRepository.findAllById(unavailableRoomsIds);
                    break;

                default:
                    List<Room> availableRoomsList = findAvailableRooms(checkin, checkout);
            }


        }


        return result;

    }

    public List<Integer> getRoomsIds(LocalDate checkin, LocalDate checkout, String status) {
        String hql = "SELECT b FROM Booking b LEFT JOIN Invoice i ON i.bookingId.id = b.id " +
                "WHERE (b.checkinDate <= :checkin AND b.checkoutDate >= :checkout) AND i.invoiceStatus = '" + status + "'";
        TypedQuery<Booking> query = entityManager.createQuery(hql, Booking.class);
        query.setParameter("checkin", checkin);
        query.setParameter("checkout", checkout);
        List<Booking> reservedBookings = query.getResultList();
        return reservedBookings.stream()
                .map(Booking::getRoomId)
                .distinct()
                .collect(Collectors.toList());

    }

}



