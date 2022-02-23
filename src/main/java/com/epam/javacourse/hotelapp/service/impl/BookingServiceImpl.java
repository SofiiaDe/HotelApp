package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.dto.ApplicationDto;
import com.epam.javacourse.hotelapp.dto.BookingDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.Booking;
import com.epam.javacourse.hotelapp.repository.BookingRepository;
import com.epam.javacourse.hotelapp.service.interfaces.IBookingService;
import com.epam.javacourse.hotelapp.service.interfaces.IRoomService;
import com.epam.javacourse.hotelapp.utils.enums.BookingStatus;
import com.epam.javacourse.hotelapp.utils.mappers.ApplicationMapper;
import com.epam.javacourse.hotelapp.utils.mappers.BookingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements IBookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    IRoomService roomService;

    @Override
    public List<BookingDto> getUserDetailedBookings(int userID) throws AppException {

        List<BookingDto> allUserBookings = new ArrayList<>();
        BookingMapper mapper = new BookingMapper();
        ArrayList<BookingDto> result;

        try {

            List<Booking> userBookings = bookingRepository.findBookingsByUserId(userID);
            userBookings.forEach(x -> allUserBookings.add(mapper.mapToDto(x)));
            result = new ArrayList<>();

        } catch (DBException exception) {
            throw new AppException("Can't retrieve list of client's bookings to show in the client's account", exception);
        }

        for (BookingDto booking : allUserBookings) {
//            var room = roomService.getRoomById(booking.getRoomId()).get();
            result.add(
                    new BookingDto(booking.getId(),
                            booking.getCheckinDate(),
                            booking.getCheckoutDate(),
                            booking.getRoomTypeBySeats(),
                            booking.getRoomClass(),
//                            room.getRoomTypeBySeats(),
//                            room.getRoomClass(),
                            false
                    ));
        }
        return result;

    }


//    @Override
//    public List<BookingDto> getAllDetailedBookings(BookingStatus bookingStatus) throws AppException {
//        List<Booking> allBookings;
//        List<User> users;
//        ArrayList<BookingDetailed> result;
//        List<Invoice> invoices;
//
//        try {
//            allBookings = this.bookingDAO.findAllBookingsForPage(page, pageSize, bookingStatus);
//
//            List<Integer> userIds = allBookings.stream().map(Booking::getUserId).distinct().collect(Collectors.toList());
//            users = this.userDao.findUsersByIds(userIds);
//
//            result = new ArrayList<>();
//            invoices = this.invoiceDAO.findInvoices(allBookings.stream().map(Booking::getId).collect(Collectors.toList()));
//        } catch (DBException exception) {
//            throw new AppException("Can't retrieve list of all bookings to show in the manager's account", exception);
//        }
//
//        for (Booking booking : allBookings) {
//            User bookingUser = users.stream().filter(u -> u.getId() == booking.getUserId()).findFirst().get();
//            result.add(
//                    new BookingDetailed(booking.getId(),
//                            bookingUser.getFirstName() + ' ' + bookingUser.getLastName(),
//                            bookingUser.getEmail(),
//                            booking.getCheckinDate(),
//                            booking.getCheckoutDate(),
//                            this.roomDAO.findRoomById(booking.getRoomId()).getRoomNumber(),
//                            invoices.stream().filter(i -> i.getBookingId() == booking.getId()).findFirst().get().getInvoiceStatus().equals("paid")
//                    ));
//        }
//        return result;
//    }

}
