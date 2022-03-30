package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.dto.*;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.NoSuchElementFoundException;
import com.epam.javacourse.hotelapp.model.Invoice;
import com.epam.javacourse.hotelapp.model.Room;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.BookingRepository;
import com.epam.javacourse.hotelapp.repository.InvoiceRepository;
import com.epam.javacourse.hotelapp.repository.RoomRepository;
import com.epam.javacourse.hotelapp.repository.UserRepository;
import com.epam.javacourse.hotelapp.service.interfaces.IInvoiceService;
import com.epam.javacourse.hotelapp.utils.Helpers;
import com.epam.javacourse.hotelapp.utils.mappers.BookingMapper;
import com.epam.javacourse.hotelapp.utils.mappers.InvoiceMapper;
import com.epam.javacourse.hotelapp.utils.mappers.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements IInvoiceService {

    private static final Logger logger = LogManager.getLogger(InvoiceServiceImpl.class);

    private final InvoiceRepository invoiceRepository;

    private final BookingRepository bookingRepository;

    private final RoomRepository roomRepository;

    private final UserRepository userRepository;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, BookingRepository bookingRepository,
                              RoomRepository roomRepository, UserRepository userRepository) {
        this.invoiceRepository = invoiceRepository;
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @Override
    public InvoiceDto getInvoiceById(int invoiceId) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(()->new NoSuchElementFoundException("Can't retrieve invoice with id = " + invoiceId));
        return InvoiceMapper.mapToDto(invoice);
    }

    @Override
    public List<InvoiceDto> getInvoicesByBookingsIds(List<Integer> bookingsIds) {
        List<Invoice> invoices = invoiceRepository.findInvoicesByBookingsIds(bookingsIds);
        List<InvoiceDto> result = new ArrayList<>();
        invoices.forEach(x -> result.add(InvoiceMapper.mapToDto(x)));
        return result;
    }

    @Override
    public List<InvoiceDto> getInvoicesByStatus(String status) {
        List<Invoice> invoices = invoiceRepository.findInvoicesByStatus(status);
        List<InvoiceDto> invoicesDto = new ArrayList<>();
        invoices.forEach(x -> invoicesDto.add(InvoiceMapper.mapToDto(x)));
        return invoicesDto;
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
                UserDto bookingUser = users.stream()
                        .filter(u -> u.getId() == invoice.getUserId().getId())
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementFoundException("Can't get bookingUser with id = " + invoice.getUserId().getId()));

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
        } catch (Exception exception) {
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
                        .orElseThrow(() -> new NoSuchElementFoundException("Can't get booking with id = " + invoice.getBookingId().getId()));

                Room room = roomRepository.getById(booking.getRoomId());

                result.add(
                        new InvoiceClientDto(invoice.getId(),
                                invoice.getInvoiceDate(),
                                Helpers.getInvoiceDueDate(InvoiceMapper.mapToDto(invoice)),
                                invoice.getAmount(),
                                invoice.getBookingId().getId(),
                                room.getPrice(),
                                booking.getCheckin(),
                                booking.getCheckout(),
                                invoice.getInvoiceStatus()
                        ));
            }
            return result;
        } catch (Exception exception) {
            throw new AppException("Can't retrieve client's invoices to show in the client's account", exception);
        }
    }

    /**
     * Execute at 3 am every day.
     *
     */
    @Transactional
    @Scheduled(cron = "0 0 3 * * *", zone = "Europe/Sofia") // The pattern is: second, minute, hour, day, month, weekday
    @Override
    public void updateInvoiceStatusToCancelled() throws AppException {
        try {
            List<Invoice> allInvoices = invoiceRepository.findAll();
            for (Invoice invoice : allInvoices) {
                if (invoice.getInvoiceStatus().equals("new") &&
                        invoice.getDueDate().isBefore(LocalDate.now())) {
                    invoiceRepository.updateInvoiceStatus("cancelled", invoice.getId());
                }
            }
        } catch (Exception exception) {
            throw new AppException("Scheduler can't cancel unpaid invoice", exception);
        }

        logger.info("Daily invoice updates were completed by scheduler");
    }

}
