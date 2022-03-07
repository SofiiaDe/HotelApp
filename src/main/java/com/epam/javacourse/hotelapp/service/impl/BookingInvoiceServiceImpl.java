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
import com.epam.javacourse.hotelapp.utils.mappers.BookingMapper;
import com.epam.javacourse.hotelapp.utils.mappers.InvoiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Period;

@Service
public class BookingInvoiceServiceImpl implements IBookingInvoiceService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    RoomRepository roomRepository;



//    public void A(dto){
//        var invoice = new Invoice();
//        var booking = new Booking();
//        invoice.setBookingId(booking);
//
//        invoiceRepo.save(invoice);
//    }

    @Override
    @Transactional
    public void createBookingAndInvoice(BookingDto booking, InvoiceDto invoice) throws AppException {

            Booking newBooking = BookingMapper.mapFromDto(booking);
            Invoice newInvoice = InvoiceMapper.mapFromDto(invoice);
            bookingRepository.save(newBooking);
            newInvoice.setBookingId(newBooking);// ???

            invoiceRepository.save(newInvoice);

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
//            throw new AppException("Can't calculate invoice amount", exception);

    }
}
