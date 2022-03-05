package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.model.Booking;
import com.epam.javacourse.hotelapp.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

        String availableRoomsHql = "SELECT r FROM Room r WHERE r.roomStatus = 'available'";
        TypedQuery<Room> query = entityManager.createQuery(availableRoomsHql, Room.class);
        List<Room> availableRooms = query.getResultList();

        List<Integer> test = getOccupiedRoomsIds(checkin, checkout);

        var result = availableRooms.stream()
                .filter(room -> !test.contains(room.getId())).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<Room> findAvailablePageableRooms(LocalDate checkin, LocalDate checkout, int pageSize, int page) {

        String availableRoomsHql = "SELECT r FROM Room r WHERE r.roomStatus = 'available'";
        TypedQuery<Room> query = entityManager.createQuery(availableRoomsHql, Room.class);
        List<Room> availableRooms = query.getResultList();

        List<Integer> test = getOccupiedRoomsIds(checkin, checkout);

        var result = availableRooms.stream()
                .filter(room -> !test.contains(room.getId())).skip(pageSize * (page - 1)).limit(pageSize).collect(Collectors.toList());

        return result;

    }
}



