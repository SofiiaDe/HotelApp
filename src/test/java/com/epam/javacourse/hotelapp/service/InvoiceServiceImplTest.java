package com.epam.javacourse.hotelapp.service;

import com.epam.javacourse.hotelapp.dto.InvoiceClientDto;
import com.epam.javacourse.hotelapp.dto.InvoiceDto;
import com.epam.javacourse.hotelapp.dto.InvoiceManagerDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.NoSuchElementFoundException;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;


    @Test
    void testGetAllDetailedInvoices_returnsCorrectData() throws AppException {

        List<Invoice> invoiceDb = EntityHelperForTests.getInvoices();
        List<User> userDb = EntityHelperForTests.getUsers();
        invoiceDb.get(0).setUserId(userDb.get(0));
        invoiceDb.get(1).setUserId(userDb.get(1));
        invoiceDb.get(0).setBookingId(EntityHelperForTests.getBookings().get(0));
        invoiceDb.get(1).setBookingId(EntityHelperForTests.getBookings().get(1));
        when(invoiceRepositoryMock.findAll()).thenReturn(invoiceDb);
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
        when(invoiceRepositoryMock.findInvoicesByStatus("new")).thenReturn(null);
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
        List<Invoice> invoicesDb = EntityHelperForTests.getInvoices();
        Invoice invoice = invoicesDb.get(0);
        invoice.setUserId(EntityHelperForTests.getUsers().get(0));
        invoice.setBookingId(EntityHelperForTests.getBookings().get(0));

        Invoice invoice2 = invoicesDb.get(1);
        invoice2.setUserId(EntityHelperForTests.getUsers().get(1));
        invoice2.setBookingId(EntityHelperForTests.getBookings().get(1));
        when(invoiceRepositoryMock.findInvoicesByStatus("new")).thenReturn(invoicesDb);


        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null,
                null, userRepositoryMock);
        invoiceService.updateInvoiceStatusToCancelled();
        verify(invoiceRepositoryMock, times(1)).updateInvoiceStatus("cancelled", invoice.getId());
    }

    @Test
    void testUpdateInvoiceStatusToCancelled_whenRepoThrows_throwsException() {
        when(invoiceRepositoryMock.findInvoicesByStatus("new")).thenReturn(null);
        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null,
                null, null);
        assertThrowsExactly(AppException.class, () -> invoiceService.updateInvoiceStatusToCancelled());
    }

    @Test
    void testGetInvoiceById_whenCalled_RepoCalled() {
        int invoiceId = 112567890;
        Invoice invoice = EntityHelperForTests.getInvoices().get(0);
        invoice.setUserId(EntityHelperForTests.getUsers().get(0));
        invoice.setBookingId(EntityHelperForTests.getBookings().get(0));
        when(invoiceRepositoryMock.findById(any())).thenReturn(Optional.of(invoice));

        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null,
                null, null);

        invoiceService.
                getInvoiceById(invoiceId);

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
        Invoice expectedInvoice = EntityHelperForTests.getInvoices().get(0);
        expectedInvoice.setUserId(EntityHelperForTests.getUsers().get(0));
        expectedInvoice.setBookingId(EntityHelperForTests.getBookings().get(0));
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
        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepositoryMock, null,
                null, null);

        invoiceService.getInvoicesByStatus("cancelled");
        verify(invoiceRepositoryMock, times(1)).findInvoicesByStatus("cancelled");
    }
}
