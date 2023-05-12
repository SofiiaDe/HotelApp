package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.dto.*;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.NoSuchElementFoundException;
import com.epam.javacourse.hotelapp.model.Booking;
import com.epam.javacourse.hotelapp.model.Invoice;
import com.epam.javacourse.hotelapp.model.Room;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.BookingRepository;
import com.epam.javacourse.hotelapp.repository.InvoiceRepository;
import com.epam.javacourse.hotelapp.repository.RoomRepository;
import com.epam.javacourse.hotelapp.repository.UserRepository;
import com.epam.javacourse.hotelapp.service.interfaces.IBookingService;
import com.epam.javacourse.hotelapp.utils.enums.BookingStatus;
import com.epam.javacourse.hotelapp.utils.mappers.BookingMapper;
import com.epam.javacourse.hotelapp.utils.mappers.InvoiceMapper;
import com.epam.javacourse.hotelapp.utils.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements IBookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, RoomRepository roomRepository,
                              UserRepository userRepository, InvoiceRepository invoiceRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.invoiceRepository = invoiceRepository;
    }


    @Override
    public List<BookingDto> getBookingsByUserId(int userId) {
        List<Booking> bookings = bookingRepository.findBookingsByUserId(userId);
        List<BookingDto> result = new ArrayList<>();
        bookings.forEach(x -> result.add(BookingMapper.mapToDto(x)));
        return result;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void createBooking(BookingDto bookingDto) {
        bookingRepository.save(BookingMapper.mapFromDto(bookingDto));
    }

    @Override
    public BookingDto getBookingById(int bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementFoundException("Can't retrieve booking with id = " + bookingId));

        return BookingMapper.mapToDto(booking);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void updateBookingStatus(BookingDto bookingDto, boolean status) {
        bookingRepository.updateBookingStatus(status, bookingDto.getId());
    }

    @Override
    public List<BookingClientDto> getUserDetailedBookings(int userID) throws AppException {

        List<Booking> allUserBookings;
        ArrayList<BookingClientDto> result;

        try {
            allUserBookings = bookingRepository.findBookingsByUserId(userID);
            result = new ArrayList<>();


            for (Booking booking : allUserBookings) {
                var room = roomRepository.getById(booking.getRoomId());
                result.add(
                        new BookingClientDto(booking.getId(),
                                booking.getCheckinDate(),
                                booking.getCheckoutDate(),
                                room.getRoomSeats(),
                                room.getRoomClass(),
                                false
                        ));
            }
        } catch (Exception exception) {
            throw new AppException("Can't retrieve list of client's bookings to show in the client's account", exception);
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
            users = userRepository.findUsersByIds(userIds);


            result = new ArrayList<>();

            invoices = invoiceRepository.findInvoicesByBookingsIds(allBookings.stream()
                    .map(Booking::getId)
                    .collect(Collectors.toList()));

            invoiceDtos = invoices.stream()
                    .map(InvoiceMapper::mapToDto)
                    .collect(Collectors.toList());

            for (Booking booking : allBookings) {
                User user = users.stream()
                        .filter(u -> u.getId() == booking.getUserId().getId())
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementFoundException("Can't get booking user with id = " + booking.getUserId().getId()));
                UserDto bookingUser = UserMapper.mapToDto(user);

                Room room = roomRepository.getById(booking.getRoomId());
                var tempInvoice = invoiceDtos.stream().filter(i -> i.getBookingId() == booking.getId())
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementFoundException("Can't get invoice"));

                result.add(
                        new BookingManagerDto(booking.getId(),
                                bookingUser.getFirstName() + ' ' + bookingUser.getLastName(),
                                bookingUser.getEmail(),
                                booking.getCheckinDate(),
                                booking.getCheckoutDate(),
                                room.getRoomNumber(),
                                tempInvoice.getStatus().equals("paid")
                        ));
            }
        } catch (Exception exception) {
            throw new AppException("Can't retrieve list of all bookings to show in the manager's account", exception);
        }
        return result;
    }
}
