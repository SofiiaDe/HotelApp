package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.dto.*;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.Booking;
import com.epam.javacourse.hotelapp.model.Invoice;
import com.epam.javacourse.hotelapp.model.Room;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.BookingRepository;
import com.epam.javacourse.hotelapp.repository.InvoiceRepository;
import com.epam.javacourse.hotelapp.repository.RoomRepository;
import com.epam.javacourse.hotelapp.repository.UserRepository;
import com.epam.javacourse.hotelapp.service.interfaces.IBookingService;
import com.epam.javacourse.hotelapp.service.interfaces.IInvoiceService;
import com.epam.javacourse.hotelapp.service.interfaces.IRoomService;
import com.epam.javacourse.hotelapp.service.interfaces.IUserService;
import com.epam.javacourse.hotelapp.utils.enums.BookingStatus;
import com.epam.javacourse.hotelapp.utils.mappers.BookingMapper;
import com.epam.javacourse.hotelapp.utils.mappers.InvoiceMapper;
import com.epam.javacourse.hotelapp.utils.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements IBookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InvoiceRepository invoiceRepository;


    @Override
    public List<BookingDto> getBookingsByUserId(int userId) throws AppException {
        List<Booking> bookings = bookingRepository.findBookingsByUserId(userId);
        List<BookingDto> result = new ArrayList<>();
        bookings.forEach(x -> result.add(BookingMapper.mapToDto(x)));
        return result;
    }


    @Override
    public List<BookingClientDto> getUserDetailedBookings(int userID) throws AppException {

        List<Booking> allUserBookings;
        ArrayList<BookingClientDto> result;

        try {
            allUserBookings = bookingRepository.findBookingsByUserId(userID);
            result = new ArrayList<>();

        } catch (DBException exception) {
            throw new AppException("Can't retrieve list of client's bookings to show in the client's account", exception);
        }

        for (Booking booking : allUserBookings) {
//            Room room = null;
            var room = roomRepository.getById(booking.getRoomId());

            result.add(
//            var optionalRoom = roomService.getRoomById(booking.getRoomId());
//            if(optionalRoom.isPresent()) {
//                room = optionalRoom.get();
//            }
//            result.add(
                    new BookingClientDto(booking.getId(),
                            booking.getCheckinDate(),
                            booking.getCheckoutDate(),
                            room.getRoomTypeBySeats(),
                            room.getRoomClass(),
                            false
                    ));
        }
        return result;

    }


    @Override
    public List<BookingManagerDto> getAllDetailedBookings(BookingStatus bookingStatus) throws AppException {
        List<Booking> allBookings;
        List<User> users;
        ArrayList<BookingManagerDto> result;
        List<InvoiceDto> invoiceDtos;
        List<Invoice> invoices;

        try {
            allBookings = bookingRepository.findAll();

            List<Integer> userIds = allBookings.stream().map(Booking::getUserId)
                    .map(User::getId)
                    .distinct().collect(Collectors.toList());
//            users = userService.getUsersByIds(userIds);
            users = userRepository.findUsersByIds(userIds);


            result = new ArrayList<>();

            invoices = invoiceRepository.findInvoicesByBookingsIds(allBookings.stream()
                            .map(Booking::getId)
                            .collect(Collectors.toList()));

            invoiceDtos = invoices.stream()
                    .map(InvoiceMapper::mapToDto)
                    .collect(Collectors.toList());

//            invoiceDtos = (List<InvoiceDto>) invoiceMapper.mapToDto(
//                    invoiceRepository.findInvoicesByBookingsIds(allBookings.stream()
//                            .map(Booking::getId)
//                            .collect(Collectors.toList())));


        for (Booking booking : allBookings) {
            UserDto bookingUser = UserMapper.mapToDto(
                    users.stream()
                    .filter(u -> u.getId() == booking.getUserId().getId())
                            .findFirst().get());
//            Optional<Room> optionalRoom = roomRepository.getById(booking.getRoomId());
//            if (optionalRoom.isEmpty()) {
//                throw new ChangeSetPersister.NotFoundException();
//            }
//            Room room = optionalRoom.get();

            Room room = roomRepository.getById(booking.getRoomId());

            result.add(
                    new BookingManagerDto(booking.getId(),
                            bookingUser.getFirstName() + ' ' + bookingUser.getLastName(),
                            bookingUser.getEmail(),
                            booking.getCheckinDate(),
                            booking.getCheckoutDate(),
                            room.getRoomNumber(),
                            invoiceDtos.stream().filter(i -> i.getBookingId() == booking.getId()).findFirst().get().getStatus().equals("paid")
                    ));
        }
        } catch (DBException exception) {
            throw new AppException("Can't retrieve list of all bookings to show in the manager's account", exception);
        }
        return result;
    }

}
