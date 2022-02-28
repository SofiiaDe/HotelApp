package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.dto.*;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.DBException;
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
import com.epam.javacourse.hotelapp.utils.mappers.BookingMapper;
import com.epam.javacourse.hotelapp.utils.mappers.InvoiceMapper;
import com.epam.javacourse.hotelapp.utils.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements IInvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;


    @Override
    public List<InvoiceDto> getInvoicesByBookingsIds(List<Integer> bookingsIds) throws AppException {
        List<Invoice> invoices = invoiceRepository.findInvoicesByBookingsIds(bookingsIds);
        List<InvoiceDto> result = new ArrayList<>();
        invoices.forEach(x -> result.add(InvoiceMapper.mapToDto(x)));
        return result;
    }

    @Override
    public LocalDate getInvoiceDueDate(InvoiceDto invoice) {
        LocalDate invoiceDate = invoice.getInvoiceDate();
        return invoiceDate.plusDays(2);

    }

    @Override
    public List<InvoiceManagerDto> getAllDetailedInvoices() throws AppException {
        try {
            List<Invoice> allInvoices = invoiceRepository.findAll();

            if (allInvoices.isEmpty()) {
                return Collections.emptyList();
            }

            List<Integer> userIds = allInvoices.stream()
                    .map(Invoice::getUserId)
                    .map(User::getId)
                    .distinct()
                    .collect(Collectors.toList());
            List<UserDto> users = userRepository.findUsersByIds(userIds).stream()
                    .map(UserMapper::mapToDto)
                    .collect(Collectors.toList());

            ArrayList<InvoiceManagerDto> result = new ArrayList<>();

            for (Invoice invoice : allInvoices) {
                UserDto bookingUser = users.stream().filter(u -> u.getId() == invoice.getUserId().getId()).findFirst().get();
                result.add(
                        new InvoiceManagerDto(invoice.getId(),
                                bookingUser.getFirstName() + ' ' + bookingUser.getLastName(),
                                bookingUser.getEmail(),
                                invoice.getAmount(),
                                invoice.getBookingId().getId(),
                                invoice.getInvoiceDate(),
                                invoice.getInvoiceStatus()
                        ));
            }
            return result;
        } catch (DBException exception) {
            throw new AppException("Can't retrieve all invoices to show in the manager's account", exception);
        }
    }

    @Override
    public List<InvoiceClientDto> getUserDetailedInvoices(int userID) throws AppException {

        try {
            List<Invoice> allUserInvoices = invoiceRepository.findInvoicesByUserId(userID);

            if (allUserInvoices.isEmpty()) {
                return Collections.emptyList();
            }

            List<BookingDto> userBookings = bookingRepository.findBookingsByUserId(userID)
                    .stream()
                    .map(BookingMapper::mapToDto)
                    .collect(Collectors.toList());

            List<InvoiceClientDto> result = new ArrayList<>();

            for (Invoice invoice : allUserInvoices) {
                BookingDto booking = userBookings.stream()
                        .filter(b -> b.getId() == invoice.getBookingId().getId())
                        .findFirst()
                        .get();
//                Optional<Room> optionalRoom = roomService.getRoomById(booking.getRoomId());
//                if (optionalRoom.isEmpty()) {
//                    throw new ChangeSetPersister.NotFoundException();
//                }
//                Room room = optionalRoom.get();

                Room room = roomRepository.getById(booking.getRoomId());

                result.add(
                        new InvoiceClientDto(invoice.getId(),
                                invoice.getInvoiceDate(),
                                getInvoiceDueDate(InvoiceMapper.mapToDto(invoice)),
                                invoice.getAmount(),
                                invoice.getBookingId().getId(),
                                room.getPrice(),
                                booking.getCheckinDate(),
                                booking.getCheckoutDate(),
                                invoice.getInvoiceStatus()
                        ));
            }
            return result;
        } catch (DBException exception) {
            throw new AppException("Can't retrieve client's invoices to show in the client's account", exception);
        }
    }

}
