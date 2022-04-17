package com.epam.javacourse.hotelapp.service;

import com.epam.javacourse.hotelapp.dto.BookingDto;
import com.epam.javacourse.hotelapp.dto.InvoiceDto;
import com.epam.javacourse.hotelapp.model.Booking;
import com.epam.javacourse.hotelapp.model.Invoice;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.BookingRepository;
import com.epam.javacourse.hotelapp.repository.InvoiceRepository;
import com.epam.javacourse.hotelapp.service.impl.BookingInvoiceServiceImpl;
import com.epam.javacourse.hotelapp.service.interfaces.IBookingService;
import com.epam.javacourse.hotelapp.service.interfaces.IInvoiceService;
import com.epam.javacourse.hotelapp.service.interfaces.IUserService;
import com.epam.javacourse.hotelapp.utils.mappers.BookingMapper;
import com.epam.javacourse.hotelapp.utils.mappers.InvoiceMapper;
import com.epam.javacourse.hotelapp.utils.mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BookingInvoiceServiceImplTest {

    @Mock
    private BookingRepository bookingRepositoryMock;

    @Mock
    private InvoiceRepository invoiceRepositoryMock;

    @Mock
    private IInvoiceService invoiceServiceMock;

    @Mock
    private IUserService userServiceMock;

    @Mock
    private IBookingService bookingServiceMock;

    @Test
    void testCreateBookingAndInvoice_whenCalled_RepoCalled() {

        BookingInvoiceServiceImpl bookInvService = new BookingInvoiceServiceImpl(bookingRepositoryMock,
                invoiceRepositoryMock, null, null, null, null);
        bookInvService.createBookingAndInvoice(getBookingDto(), getInvoiceDto());

        verify(bookingRepositoryMock, times(1)).save(any(Booking.class));
        verify(invoiceRepositoryMock, times(1)).save(any(Invoice.class));

    }


    @Test
    void testPayInvoice_whenCalled_RepoCalled() {

        Invoice invoiceToPay = EntityHelperForTests.getInvoices().get(0);
        Booking booking = EntityHelperForTests.getBookings().get(0);
        User user = EntityHelperForTests.getUsers().get(0);
        invoiceToPay.setBookingId(booking);
        invoiceToPay.setUserId(user);
        booking.setUserId(user);

        BookingInvoiceServiceImpl bookInvService = new BookingInvoiceServiceImpl(bookingRepositoryMock,
                invoiceRepositoryMock, null, invoiceServiceMock, bookingServiceMock, userServiceMock);

        when(invoiceServiceMock.getInvoiceById(invoiceToPay.getId())).thenReturn(InvoiceMapper.mapToDto(invoiceToPay));
        when(userServiceMock.getUserById(user.getId())).thenReturn(UserMapper.mapToDto(user));
        when(bookingServiceMock.getBookingById(booking.getId())).thenReturn(BookingMapper.mapToDto(booking));

        bookInvService.payInvoice(invoiceToPay.getId());
        verify(bookingRepositoryMock, times(1)).updateBookingStatus(true, invoiceToPay.getBookingId().getId());
        verify(invoiceRepositoryMock, times(1)).updateInvoiceStatus("paid", invoiceToPay.getId());
    }

    private BookingDto getBookingDto() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(111);
        bookingDto.setRoomId(456);
        bookingDto.setCheckin(LocalDate.of(2022, 5, 29));
        bookingDto.setCheckout(LocalDate.of(2022, 5, 30));
        bookingDto.setUserId(77);
        return bookingDto;
    }

    private InvoiceDto getInvoiceDto() {

        InvoiceDto invoice = new InvoiceDto();
        invoice.setId(111);
        invoice.setInvoiceDate(LocalDate.MIN);
        invoice.setAmount(new BigDecimal("300.00"));
        invoice.setUserId(1);
        invoice.setStatus("new");

        return invoice;
    }
}
