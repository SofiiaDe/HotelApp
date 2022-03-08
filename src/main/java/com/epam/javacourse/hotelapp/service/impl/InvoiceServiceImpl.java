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
import com.epam.javacourse.hotelapp.service.interfaces.IInvoiceService;
import com.epam.javacourse.hotelapp.utils.Helpers;
import com.epam.javacourse.hotelapp.utils.mappers.BookingMapper;
import com.epam.javacourse.hotelapp.utils.mappers.InvoiceMapper;
import com.epam.javacourse.hotelapp.utils.mappers.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements IInvoiceService {

    private static final Logger logger = LogManager.getLogger(InvoiceServiceImpl.class);

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
        } catch (DBException exception) {
            throw new AppException("Can't retrieve client's invoices to show in the client's account", exception);
        }
    }

    /**
     * Execute at 12 pm every day.
     *
     * @throws AppException
     */
    @Scheduled(cron = "0 0 12 * * ?")
    //@Scheduled(cron = "@daily"
    @Override
    public void updateInvoiceStatusToCancelled() throws AppException {
        try {
            List<Invoice> allInvoices = invoiceRepository.findAll();
            List<InvoiceDto> invoicesDto = new ArrayList<>();
            allInvoices.forEach(x -> invoicesDto.add(InvoiceMapper.mapToDto(x)));

            for (InvoiceDto invoice : invoicesDto) {
                if (invoice.getStatus().equals("new") &&
                        Helpers.getInvoiceDueDate(invoice).isBefore(LocalDate.now())) {
                    invoice.setStatus("cancelled");
                    invoiceRepository.updateInvoiceStatus("cancelled", invoice.getId());
                }
            }
        } catch (DBException exception) {
            throw new AppException("Scheduler can't cancel unpaid invoice", exception);
        }

        logger.info("Daily invoice updates were completed by scheduler");
    }

    @Transactional
    @Override
    public void payInvoice(int invoiceId) throws AppException {
        try {
            Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoiceId);

            if (optionalInvoice.isEmpty()) {
                throw new ChangeSetPersister.NotFoundException();
            }
            Invoice invoiceToBePaid = optionalInvoice.get();
            invoiceToBePaid.setInvoiceStatus("paid");
            invoiceRepository.updateInvoiceStatus("paid", invoiceToBePaid.getId());

        } catch (DBException | ChangeSetPersister.NotFoundException exception) {
            throw new AppException("Can't pay invoice", exception);
        }
    }



}
