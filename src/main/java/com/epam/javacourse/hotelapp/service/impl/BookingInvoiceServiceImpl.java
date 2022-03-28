package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.dto.BookingDto;
import com.epam.javacourse.hotelapp.dto.InvoiceDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.Booking;
import com.epam.javacourse.hotelapp.model.Invoice;
import com.epam.javacourse.hotelapp.model.Room;
import com.epam.javacourse.hotelapp.repository.BookingRepository;
import com.epam.javacourse.hotelapp.repository.InvoiceRepository;
import com.epam.javacourse.hotelapp.repository.RoomRepository;
import com.epam.javacourse.hotelapp.service.interfaces.IBookingInvoiceService;
import com.epam.javacourse.hotelapp.service.interfaces.IBookingService;
import com.epam.javacourse.hotelapp.service.interfaces.IInvoiceService;
import com.epam.javacourse.hotelapp.service.interfaces.IUserService;
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class BookingInvoiceServiceImpl implements IBookingInvoiceService {

    private static final Logger logger = LogManager.getLogger(BookingInvoiceServiceImpl.class);

    private final BookingRepository bookingRepository;

    private final InvoiceRepository invoiceRepository;

    private final RoomRepository roomRepository;

    private final IInvoiceService invoiceService;

    private final IBookingService bookingService;

    private final IUserService userService;

    @Autowired
    public BookingInvoiceServiceImpl(BookingRepository bookingRepository, InvoiceRepository invoiceRepository,
                                     RoomRepository roomRepository, IInvoiceService invoiceService,
                                     IBookingService bookingService, IUserService userService) {
        this.bookingRepository = bookingRepository;
        this.invoiceRepository = invoiceRepository;
        this.roomRepository = roomRepository;
        this.invoiceService = invoiceService;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void createBookingAndInvoice(BookingDto booking, InvoiceDto invoice) throws AppException {

        Booking newBooking = BookingMapper.mapFromDto(booking);
        Invoice newInvoice = InvoiceMapper.mapFromDto(invoice);
        bookingRepository.save(newBooking);
        newInvoice.setBookingId(newBooking);
        newInvoice.setDueDate(Helpers.getInvoiceDueDate(invoice));
        invoiceRepository.save(newInvoice);

        logger.info("Create booking with id = {}", newBooking.getId());
        logger.info("Create invoice with id = {}", newInvoice.getId());

    }

    @Override
    public BigDecimal getInvoiceAmount(BookingDto booking) throws AppException {


        LocalDate checkinDate = booking.getCheckin();
        LocalDate checkoutDate = booking.getCheckout();
        Period period = Period.between(checkinDate, checkoutDate);
        Room room = roomRepository.getById(booking.getRoomId());

        // initialize amount as 0 for a default value
        BigDecimal amount = new BigDecimal(BigInteger.ZERO, 2);

        // number of days is converted to BigDecimal
        BigDecimal totalCost = room.getPrice().multiply(new BigDecimal(Math.abs(period.getDays())));
        amount = amount.add(totalCost);

        return amount;
    }

    /**
     * Execute at 1 am every day.
     *
     * @throws AppException
     */
    @Transactional
    @Scheduled(cron = "0 0 1 * * *", zone = "Europe/Sofia")
    // The pattern is: second, minute, hour, day, month, weekday
    @Override
    public void cancelUnpaidBookings() throws AppException {

        try {
            List<InvoiceDto> unpaidInvoicesDto = invoiceService.getInvoicesByStatus("cancelled");

            for (InvoiceDto invoice : unpaidInvoicesDto) {
                BookingDto bookingDto = bookingService.getBookingById(invoice.getBookingId());
                bookingService.updateBookingStatus(bookingDto, false);
            }
        } catch (Exception exception) {
            throw new AppException("Scheduler can't cancel unpaid booking", exception);
        }
        logger.info("Daily booking updates were completed by scheduler");
    }

    @Transactional
    @Override
    public void payInvoice(int invoiceId) throws AppException {
        try {
            InvoiceDto invoiceDto = invoiceService.getInvoiceById(invoiceId);
            invoiceDto.setUser(UserMapper.mapFromDto(userService.getUserById(invoiceDto.getUserId())));
            invoiceDto.setBooking(BookingMapper.mapFromDto(bookingService.getBookingById(invoiceDto.getBookingId())));
            invoiceRepository.updateInvoiceStatus("paid", invoiceId);
            bookingRepository.updateBookingStatus(true, invoiceDto.getBookingId());
        } catch (Exception exception) {
            throw new AppException("Can't pay invoice", exception);
        }
    }
}
