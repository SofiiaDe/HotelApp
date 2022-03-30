package com.epam.javacourse.hotelapp.service;

import com.epam.javacourse.hotelapp.dto.BookingDto;
import com.epam.javacourse.hotelapp.dto.BookingManagerDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.NoSuchElementFoundException;
import com.epam.javacourse.hotelapp.model.Booking;
import com.epam.javacourse.hotelapp.model.Claim;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.BookingRepository;
import com.epam.javacourse.hotelapp.repository.InvoiceRepository;
import com.epam.javacourse.hotelapp.repository.UserRepository;
import com.epam.javacourse.hotelapp.service.impl.BookingServiceImpl;
import com.epam.javacourse.hotelapp.utils.mappers.BookingMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.print.Book;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.javacourse.hotelapp.utils.enums.BookingStatus.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private InvoiceRepository invoiceRepositoryMock;

    @Test
    void testCreate_whenCalled_callsRepo() {
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepositoryMock,
                null, null, null);

        bookingService.createBooking(new BookingDto());
        verify(bookingRepositoryMock, times(1)).save(any(Booking.class));
    }

    @Test
    void testGetAllDetailedBookings_whenNoBookings_returnsEmptyCollections() throws AppException {
        when(bookingRepositoryMock.findAll()).thenReturn(Collections.emptyList());

        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepositoryMock,
                null, userRepositoryMock, invoiceRepositoryMock);
        List<BookingManagerDto> result = bookingService.getAllDetailedBookings(NEW);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllDetailedBookings_whenNoBookings_userRepoNotCalled() throws AppException {
        when(bookingRepositoryMock.findAll()).thenReturn(Collections.emptyList());
        verify(userRepositoryMock, times(0)).findUsersByIds(anyList());

        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepositoryMock,
                null, userRepositoryMock, invoiceRepositoryMock);
        List<BookingManagerDto> result = bookingService.getAllDetailedBookings(NEW);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllDetailedBookings_whenRepoThrows_ShowExceptionMessage() {
        String messageNotToGet = "aaaaa";
        when(bookingRepositoryMock.findAll()).thenReturn(null);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepositoryMock,
                null, null, null);

        try {
            bookingService.getAllDetailedBookings(NEW);
        } catch (AppException ex) {
            assertEquals("Can't retrieve list of all bookings to show in the manager's account", ex.getMessage());
            assertNotEquals(messageNotToGet, ex.getMessage());
            return;
        }

        Assertions.fail("Should have thrown AppException");
    }

    @Test
    void testGetUserDetailedBookings_whenRepoThrows_ShowExceptionMessage() {
        String messageNotToGet = "aaaaa";
        int userId = 1;
        when(bookingRepositoryMock.findBookingsByUserId(userId)).thenReturn(null);
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepositoryMock, null, null, null);

        try {
            bookingService.getUserDetailedBookings(userId);
        } catch (AppException ex) {
            assertEquals("Can't retrieve list of client's bookings to show in the client's account", ex.getMessage());
            assertNotEquals(messageNotToGet, ex.getMessage());
            return;
        }

        Assertions.fail("Should have thrown AppException");
    }

    @Test
    void testGetBookingsByUserId_whenCalled_RepoCalled() {
        int userId = 7890;
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepositoryMock, null, null, null);

        bookingService.getBookingsByUserId(userId);

        verify(bookingRepositoryMock, times(1)).findBookingsByUserId(userId);
    }

    @Test
    void testGetBookingById_whenCalled_RepoCalled() {
        int bookingId = 112567890;
        Booking booking = getBookings().get(0);
        when(bookingRepositoryMock.findById(any())).thenReturn(Optional.of(booking));
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepositoryMock, null,
                userRepositoryMock, null);

        bookingService.getBookingById(bookingId);

        verify(bookingRepositoryMock, times(1)).findById(bookingId);
    }

    @Test
    void testGetBookingById_whenRepoThrows_throwsException() {
        int bookingId = 112567890;

        when(bookingRepositoryMock.findById(bookingId)).thenThrow(new NoSuchElementFoundException("Can't get booking with id = " + bookingId));
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepositoryMock, null, null, null);

        Assertions.assertThrowsExactly(NoSuchElementFoundException.class, () -> bookingService.getBookingById(bookingId));
    }

    @Test
    void testGetBookingById_whenRepoThrows_ShowExceptionMessage() {
        String messageNotToGet = "aaaaa";
        int bookingId = 112567890;

        when(bookingRepositoryMock.findById(bookingId)).thenReturn(Optional.empty());
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepositoryMock, null, null, null);

        try {
            bookingService.getBookingById(bookingId);
        } catch (Exception ex) {
            assertEquals("Can't retrieve booking with id = " + bookingId, ex.getMessage());
            assertNotEquals(messageNotToGet, ex.getMessage());
            return;
        }

        Assertions.fail("Should have thrown AppException");
    }

    @Test
    void testGetBookingById_whenCalled_returnsCorrectBooking() {
        int bookingId = 112567890;
        Booking booking = getBookings().get(0);
        when(bookingRepositoryMock.findById(bookingId)).thenReturn(Optional.of(booking));
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepositoryMock, null,
                userRepositoryMock, null);

        BookingDto result = bookingService.getBookingById(bookingId);
        BookingDto expected = BookingMapper.mapToDto(booking);

        assertEquals(expected.getCheckin(), result.getCheckin());
        assertEquals(expected.getCheckout(), result.getCheckout());
        assertEquals(expected.getRoomId(), result.getRoomId());
        assertEquals(expected.getUserId(), result.getUserId());

    }

    @Test
    void testUpdateBookingStatus_whenCalled_RepoCalled() {
        BookingDto booking = new BookingDto();
        BookingServiceImpl bookingService = new BookingServiceImpl(bookingRepositoryMock, null,
                userRepositoryMock, null);
        bookingService.updateBookingStatus(booking, true);

        verify(bookingRepositoryMock, times(1)).updateBookingStatus(true, booking.getId());
    }

    private List<Booking> getBookings() {

        List<User> users = getUsers();
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking();
        booking1.setRoomId(456);
        booking1.setCheckinDate(LocalDate.now().plusDays(7));
        booking1.setCheckoutDate(LocalDate.now().plusDays(9));
        booking1.setId(111);
        booking1.setUserId(users.get(0));

        Booking booking2 = new Booking();
        booking2.setRoomId(789);
        booking2.setCheckinDate(LocalDate.now().plusDays(4));
        booking2.setCheckoutDate(LocalDate.now().plusDays(6));
        booking2.setId(222);
        booking2.setUserId(users.get(1));

        bookings.add(booking1);
        bookings.add(booking2);

        return bookings;
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
}
