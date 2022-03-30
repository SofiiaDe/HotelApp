package com.epam.javacourse.hotelapp.service;

import com.epam.javacourse.hotelapp.dto.InvoiceClientDto;
import com.epam.javacourse.hotelapp.dto.InvoiceDto;
import com.epam.javacourse.hotelapp.dto.InvoiceManagerDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.NoSuchElementFoundException;
import com.epam.javacourse.hotelapp.model.Booking;
import com.epam.javacourse.hotelapp.model.Invoice;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.InvoiceRepository;
import com.epam.javacourse.hotelapp.repository.UserRepository;
import com.epam.javacourse.hotelapp.service.impl.InvoiceServiceImpl;
import com.epam.javacourse.hotelapp.utils.mappers.InvoiceMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;


    @Test
    void testGetAllDetailedInvoices_returnsCorrectData() throws AppException {
        List<Invoice> invoiceDb = getInvoices();
        when(invoiceRepositoryMock.findAll()).thenReturn(invoiceDb);

        List<User> userDb = getUsers();
        when(userRepositoryMock.findUsersByIds(anyList())).thenReturn(userDb);

        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null,
                null, userRepositoryMock);
        List<InvoiceManagerDto> result = invoiceService.getAllDetailedInvoices();

        for (InvoiceManagerDto invoiceDetails :
                result) {
            Invoice expectedInvoice = invoiceDb.stream().filter(apl -> apl.getId() == invoiceDetails.getId()).findFirst().get();
            assertEquals(invoiceDetails.getAmount(), expectedInvoice.getAmount());
            assertEquals(invoiceDetails.getBookingId(), expectedInvoice.getBookingId().getId());
            assertEquals(invoiceDetails.getInvoiceDate(), expectedInvoice.getInvoiceDate());
            assertEquals(invoiceDetails.getStatus(), expectedInvoice.getInvoiceStatus());

            User expectedUser = userDb.stream().filter(u -> Objects.equals(u.getEmail(), invoiceDetails.getBookedByUserEmail())).findFirst().get();
            assertEquals(invoiceDetails.getBookedByUser(), expectedUser.getFirstName() + ' ' + expectedUser.getLastName());
        }
    }

    @Test
    void testGetAllDetailedInvoices_whenNoInvoices_returnsEmptyCollections() throws AppException {
        when(invoiceRepositoryMock.findAll()).thenReturn(Collections.emptyList());

        InvoiceServiceImpl InvoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null, null, null);
        List<InvoiceManagerDto> result = InvoiceService.getAllDetailedInvoices();

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllDetailedInvoices_whenNoInvoices_userRepoNotCalled() throws AppException {
        when(invoiceRepositoryMock.findAll()).thenReturn(Collections.emptyList());
        verify(userRepositoryMock, times(0)).findUsersByIds(anyList());

        InvoiceServiceImpl InvoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null, null, userRepositoryMock);
        List<InvoiceManagerDto> result = InvoiceService.getAllDetailedInvoices();

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllDetailedInvoices_whenRepoThrows_throwsException() {
        when(invoiceRepositoryMock.findAll()).thenReturn(null);
        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null, null, null);

        assertThrowsExactly(AppException.class, invoiceService::getAllDetailedInvoices);
    }

    @Test
    void testGetAllDetailedInvoices_whenRepoThrows_ShowExceptionMessage() {
        String messageNotToGet = "aaaaa";
        when(invoiceRepositoryMock.findAll()).thenReturn(null);
        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock,
                null, null, null);

        try {
            invoiceService.getAllDetailedInvoices();
        } catch (AppException ex) {
            assertEquals("Can't retrieve all invoices to show in the manager's account", ex.getMessage());
            Assertions.assertNotEquals(messageNotToGet, ex.getMessage());
            return;
        }

        Assertions.fail("Should have thrown AppException");
    }

    @Test
    void testGetUserDetailedInvoices_whenNoInvoices_returnsEmptyCollections() throws AppException {
        int userId = 1;
        when(invoiceRepositoryMock.findInvoicesByUserId(userId)).thenReturn(Collections.emptyList());

        InvoiceServiceImpl InvoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null,
                null, userRepositoryMock);
        List<InvoiceClientDto> result = InvoiceService.getUserDetailedInvoices(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetUserDetailedInvoices_whenRepoThrows_throwsException() {
        int userId = 1;
        when(invoiceRepositoryMock.findInvoicesByUserId(userId)).thenReturn(null);
        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null,
                null, null);

        assertThrowsExactly(AppException.class, () -> invoiceService.getUserDetailedInvoices(userId));
    }

    @Test
    void testGetUserDetailedInvoices_whenRepoThrows_ShowExceptionMessage() {
        String messageNotToGet = "aaaaa";
        int userId = 1;
        when(invoiceRepositoryMock.findInvoicesByUserId(userId)).thenReturn(null);
        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null,
                null, null);

        try {
            invoiceService.getUserDetailedInvoices(userId);
        } catch (AppException ex) {
            assertEquals("Can't retrieve client's invoices to show in the client's account", ex.getMessage());
            Assertions.assertNotEquals(messageNotToGet, ex.getMessage());
            return;
        }

        Assertions.fail("Should have thrown AppException");
    }

    @Test
    void testGetInvoicesByBookingsIds_whenCalled_RepoCalled() {
        List<Integer> bookingsIds = new ArrayList<>();
        bookingsIds.add(888);
        bookingsIds.add(999);
        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null,
                null, null);

        invoiceService.getInvoicesByBookingsIds(bookingsIds);
        verify(invoiceRepositoryMock, times(1)).findInvoicesByBookingsIds(bookingsIds);
    }

    @Test
    void testUpdateInvoiceStatusToCancelled_whenRepoThrows_ShowExceptionMessage() {
        String messageNotToGet = "aaaaa";
        when(invoiceRepositoryMock.findAll()).thenReturn(null);
        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null,
                null, null);

        try {
            invoiceService.updateInvoiceStatusToCancelled();
        } catch (AppException ex) {
            assertEquals("Scheduler can't cancel unpaid invoice", ex.getMessage());
            assertNotEquals(messageNotToGet, ex.getMessage());
            return;
        }

        Assertions.fail("Should have thrown AppException");
    }

    @Test
    void testUpdateInvoiceStatusToCancelled_whenCalled_callsRepo() throws AppException {
        List<Invoice> invoicesDb = getInvoices();
        Invoice invoice = invoicesDb.get(0);
        when(invoiceRepositoryMock.findAll()).thenReturn(invoicesDb);

        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null,
                null, null);
        invoiceService.updateInvoiceStatusToCancelled();
        verify(invoiceRepositoryMock, times(1)).updateInvoiceStatus("cancelled", invoice.getId());
    }

    @Test
    void testUpdateInvoiceStatusToCancelled_whenRepoThrows_throwsException() {
        when(invoiceRepositoryMock.findAll()).thenReturn(null);
        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null,
                null, null);
        assertThrowsExactly(AppException.class, () -> invoiceService.updateInvoiceStatusToCancelled());
    }

    @Test
    void testGetInvoiceById_whenCalled_RepoCalled() {
        int invoiceId = 112567890;
        Invoice invoice = getInvoices().get(0);
        when(invoiceRepositoryMock.findById(any())).thenReturn(Optional.of(invoice));

        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null,
                null, null);

        invoiceService.getInvoiceById(invoiceId);

        verify(invoiceRepositoryMock, times(1)).findById(invoiceId);
    }

    @Test
    void testGetInvoiceById_whenRepoThrows_throwsException() {
        int invoiceId = 112567890;

        when(invoiceRepositoryMock.findById(invoiceId)).thenThrow(new NoSuchElementFoundException("Can't get invoice with id = " + invoiceId));
        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null,
                null, null);

        assertThrowsExactly(NoSuchElementFoundException.class, () -> invoiceService.getInvoiceById(invoiceId));
    }

    @Test
    void testGetInvoiceById_whenRepoThrows_ShowExceptionMessage() {
        String messageNotToGet = "aaaaa";
        int invoiceId = 112567890;

        when(invoiceRepositoryMock.findById(invoiceId)).thenReturn(Optional.empty());
        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null,
                null, null);

        try {
            invoiceService.getInvoiceById(invoiceId);
        } catch (NoSuchElementFoundException ex) {
            assertEquals("Can't retrieve invoice with id = " + invoiceId, ex.getMessage());
            Assertions.assertNotEquals(messageNotToGet, ex.getMessage());
            return;
        }

        Assertions.fail("Should have thrown AppException");
    }

    @Test
    void testGetInvoiceById_whenCalled_returnsCorrectInvoice() {
        Invoice expectedInvoice = getInvoices().get(0);
        int invoiceId = expectedInvoice.getId();
        when(invoiceRepositoryMock.findById(invoiceId)).thenReturn(Optional.of(expectedInvoice));
        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null,
                null, null);

        InvoiceDto result = invoiceService.getInvoiceById(invoiceId);
        InvoiceDto expected = InvoiceMapper.mapToDto(expectedInvoice);
        assertEquals(expected.getInvoiceDate(), result.getInvoiceDate());
        assertEquals(expected.getAmount(), result.getAmount());
        assertEquals(expected.getBookingId(), result.getBookingId());
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getStatus(), result.getStatus());
        assertEquals(expected.getBooking(), result.getBooking());
    }

    @Test
    void testGetInvoicesByStatus_whenCalled_RepoCalled() {
        int userId = 7890;
        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null,
                null, null);

        invoiceService.getInvoicesByStatus("cancelled");

        verify(invoiceRepositoryMock, times(1)).findInvoicesByStatus("cancelled");
    }

    private List<Invoice> getInvoices() {

        List<User> userDb = getUsers();
        List<Booking> bookingDb = getBookings();

        List<Invoice> invoiceDb = new ArrayList<>();
        Invoice invoice = new Invoice();
        invoice.setId(111);
        invoice.setInvoiceDate(LocalDate.now().minusDays(8));
        invoice.setAmount(new BigDecimal("300.00"));
        invoice.setBookingId(bookingDb.get(0));
        invoice.setUserId(getUsers().get(0));
        invoice.setInvoiceStatus("new");
        invoice.setDueDate(LocalDate.now().minusDays(6));

        Invoice invoice2 = new Invoice();
        invoice2.setId(222);
        invoice2.setInvoiceDate(LocalDate.now().plusDays(1));
        invoice2.setAmount(new BigDecimal("200.00"));
        invoice2.setBookingId(bookingDb.get(1));
        invoice2.setUserId(userDb.get(1));
        invoice2.setInvoiceStatus("someStatus");

        invoiceDb.add(invoice);
        invoiceDb.add(invoice2);

        return invoiceDb;
    }

    private List<User> getUsers() {

        User user1 = new User();
        user1.setId(1);
        user1.setFirstName("UserFirstName");
        user1.setLastName("UserLastName");
        user1.setEmail("aaa@bbb.ccc");

        User user2 = new User();
        user2.setId(2);
        user2.setFirstName("AAAAA");
        user2.setLastName("BBBB");
        user2.setEmail("writing.tests@is.timeconsuming");

        List<User> userDb = new ArrayList<>();
        userDb.add(user1);
        userDb.add(user2);

        return userDb;
    }

    private List<Booking> getBookings() {

        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking();
        booking1.setRoomId(456);
        booking1.setCheckinDate(LocalDate.now().plusDays(7));
        booking1.setCheckoutDate(LocalDate.now().plusDays(9));
        booking1.setId(111);

        Booking booking2 = new Booking();
        booking2.setRoomId(789);
        booking2.setCheckinDate(LocalDate.now().plusDays(4));
        booking2.setCheckoutDate(LocalDate.now().plusDays(6));
        booking2.setId(222);

        bookings.add(booking1);
        bookings.add(booking2);

        return bookings;
    }
}
