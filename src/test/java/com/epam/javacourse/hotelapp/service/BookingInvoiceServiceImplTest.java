package com.epam.javacourse.hotelapp.service;

import com.epam.javacourse.hotelapp.dto.BookingDto;
import com.epam.javacourse.hotelapp.dto.InvoiceDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.Booking;
import com.epam.javacourse.hotelapp.model.Invoice;
import com.epam.javacourse.hotelapp.repository.BookingRepository;
import com.epam.javacourse.hotelapp.repository.InvoiceRepository;
import com.epam.javacourse.hotelapp.service.impl.BookingInvoiceServiceImpl;
import com.epam.javacourse.hotelapp.service.impl.InvoiceServiceImpl;
import com.epam.javacourse.hotelapp.service.interfaces.IInvoiceService;
import com.epam.javacourse.hotelapp.service.interfaces.IUserService;
import com.epam.javacourse.hotelapp.utils.mappers.BookingMapper;
import com.epam.javacourse.hotelapp.utils.mappers.InvoiceMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
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

    @Test
    void testCreateBookingAndInvoice_whenCalled_RepoCalled() throws AppException {

        BookingInvoiceServiceImpl bookInvService = new BookingInvoiceServiceImpl(bookingRepositoryMock,
                invoiceRepositoryMock, null, null, null, null);
        bookInvService.createBookingAndInvoice(getBookingDto(), getInvoiceDto());

        verify(bookingRepositoryMock, times(1)).save(any(Booking.class));
        verify(invoiceRepositoryMock, times(1)).save(any(Invoice.class));

    }

    @Test
    void testCreateBookingAndInvoice_whenRepoThrows_throwsException() {
        doReturn(null).when(bookingRepositoryMock).save(BookingMapper.mapFromDto(getBookingDto()));
        doReturn(null).when(invoiceRepositoryMock).save(InvoiceMapper.mapFromDto(getInvoiceDto()));
        BookingInvoiceServiceImpl bookInvService = new BookingInvoiceServiceImpl(bookingRepositoryMock,
                invoiceRepositoryMock, null, null, null, null);

        Assertions.assertThrowsExactly(AppException.class, () -> bookInvService.createBookingAndInvoice(new BookingDto(), new InvoiceDto()));
    }

    @Test
    void testCreateBookingAndInvoice_whenRepoThrows_ShowExceptionMessage() {
        String messageNotToGet = "aaaaa";
        doReturn(null).when(bookingRepositoryMock).save(BookingMapper.mapFromDto(getBookingDto()));
        doReturn(null).when(invoiceRepositoryMock).save(InvoiceMapper.mapFromDto(getInvoiceDto()));
        BookingInvoiceServiceImpl bookInvService = new BookingInvoiceServiceImpl(bookingRepositoryMock,
                invoiceRepositoryMock, null, null, null, null);

        try {
            bookInvService.createBookingAndInvoice(new BookingDto(), new InvoiceDto());
        } catch (AppException ex) {
            Assertions.assertEquals("Can't create new booking and invoice", ex.getMessage());
            Assertions.assertNotEquals(messageNotToGet, ex.getMessage());
            return;
        }

        Assertions.fail("Should have thrown AppException");
    }

//    @Test
//    void testPayInvoice_whenCalled_RepoCalled() throws AppException {
////        List<Invoice> invoicesDb = getInvoices();
////        int invoiceId = 555;
//        InvoiceDto invoiceToPay = getInvoiceDto();
//        Invoice invoice = InvoiceMapper.mapFromDto(invoiceToPay);
//        BookingInvoiceServiceImpl bookInvService = new BookingInvoiceServiceImpl(bookingRepositoryMock,
//                invoiceRepositoryMock, null, invoiceServiceMock, null, userServiceMock);
//        when(invoiceRepositoryMock.findById(invoice.getId())).thenReturn(Optional.of(invoice));
//        when(invoiceServiceMock.getInvoiceById(invoice.getId())).thenReturn(invoiceToPay);
//
//        bookInvService.payInvoice(invoiceToPay.getId());
//        verify(bookingRepositoryMock, times(1)).updateBookingStatus(true, invoiceToPay.getBookingId());
//        verify(invoiceRepositoryMock, times(1)).updateInvoiceStatus("paid", invoiceToPay.getId());
//    }
//
//    @Test
//    void testPayInvoice_whenRepoThrows_throwsException() throws AppException {
////        List<Invoice> invoicesDb = getInvoices();
////        Invoice invoiceToPay = invoicesDb.stream().findFirst().get();
//        Invoice invoiceToPay = InvoiceMapper.mapFromDto(getInvoiceDto());
//        when(invoiceRepositoryMock.findById(invoiceToPay.getId())).thenReturn(Optional.of(invoiceToPay));
//        doThrow(new Exception()).when(invoiceRepositoryMock).updateInvoiceStatus("paid", invoiceToPay.getId());
//        BookingInvoiceServiceImpl bookInvService = new BookingInvoiceServiceImpl(bookingRepositoryMock,
//                invoiceRepositoryMock, null, null, null, null);
//        assertThrowsExactly(AppException.class, () -> bookInvService.payInvoice(invoiceToPay.getId()));
//    }
//
//    @Test
//    void testPayInvoice_whenRepoThrows_ShowExceptionMessage() {
//        String messageNotToGet = "aaaaa";
////        List<Invoice> invoicesDb = getInvoices();
////        Invoice invoiceToPay = invoicesDb.stream().findFirst().get();
//        Invoice invoiceToPay = InvoiceMapper.mapFromDto(getInvoiceDto());
//        doThrow(new Exception(messageNotToGet)).when(invoiceRepositoryMock).updateInvoiceStatus("paid", invoiceToPay.getId());
//        when(invoiceRepositoryMock.findById(invoiceToPay.getId())).thenReturn(Optional.of(invoiceToPay));
//
//        BookingInvoiceServiceImpl bookInvService = new BookingInvoiceServiceImpl(bookingRepositoryMock,
//                invoiceRepositoryMock, null, null, null, null);
//        try {
//            bookInvService.payInvoice(invoiceToPay.getId());
//        } catch (AppException ex) {
//            assertEquals("Can't pay invoice", ex.getMessage());
//            Assertions.assertNotEquals(messageNotToGet, ex.getMessage());
//            return;
//        }
//
//        Assertions.fail("Should have thrown AppException");
//    }

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
//        invoice.setBookingId(888);
        invoice.setUserId(1);
        invoice.setStatus("new");

        return invoice;
    }
}
