package com.epam.javacourse.hotelapp.service;

import com.epam.javacourse.hotelapp.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EntityHelperForTests {

    public static List<ConfirmationRequest> getConfirmRequests() {

        List<ConfirmationRequest> confirmationRequestDb = new ArrayList<>();

        ConfirmationRequest confirmRequest = new ConfirmationRequest();
        confirmRequest.setId(111);
        confirmRequest.setConfirmRequestDate(LocalDate.now().minusDays(3));
        confirmRequest.setStatus("someStatus");

        ConfirmationRequest confirmRequest2 = new ConfirmationRequest();
        confirmRequest2.setId(222);
        confirmRequest2.setConfirmRequestDate(LocalDate.now().minusDays(1));
        confirmRequest2.setStatus("does not matter");

        confirmationRequestDb.add(confirmRequest);
        confirmationRequestDb.add(confirmRequest2);

        return confirmationRequestDb;
    }

    public static List<User> getUsers() {

        List<User> userDb = new ArrayList<>();

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

        userDb.add(user1);
        userDb.add(user2);

        return userDb;
    }

    public static List<Claim> getClaims() {

        List<Claim> claimDb = new ArrayList<>();

        Claim claim1 = new Claim();
        claim1.setId(111);
        claim1.setCheckinDate(LocalDate.MIN);
        claim1.setCheckoutDate(LocalDate.MAX);
        claim1.setRoomSeats("double");
        claim1.setRoomClass("aaaa");

        Claim claim2 = new Claim();
        claim2.setId(222);
        claim2.setCheckinDate(LocalDate.now().plusDays(1));
        claim2.setCheckinDate(LocalDate.now().plusDays(3));
        claim2.setRoomSeats("twin");
        claim2.setRoomClass("does not matter");

        claimDb.add(claim1);
        claimDb.add(claim2);

        return claimDb;
    }

    public static List<Room> getRooms() {

        List<Room> roomDb = new ArrayList<>();

        Room room1 = new Room();
        room1.setId(111);
        room1.setRoomSeats("double");
        room1.setRoomClass("business");
        room1.setRoomNumber(77);
        room1.setPrice(new BigDecimal("250.00"));

        Room room2 = new Room();
        room2.setId(222);
        room2.setRoomSeats("twin");
        room2.setRoomClass("standard");
        room2.setRoomNumber(88);
        room2.setPrice(new BigDecimal("200.00"));

        Room room3 = new Room();
        room3.setId(333);
        room3.setRoomSeats("single");
        room3.setRoomClass("lux");
        room3.setRoomNumber(77);
        room3.setPrice(new BigDecimal("300.00"));

        roomDb.add(room1);
        roomDb.add(room2);
        roomDb.add(room3);

        return roomDb;
    }

    public static List<Booking> getBookings() {

        List<Booking> bookingDb = new ArrayList<>();
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

        bookingDb.add(booking1);
        bookingDb.add(booking2);

        return bookingDb;
    }

    protected static List<Invoice> getInvoices() {

        List<Invoice> invoiceDb = new ArrayList<>();

        Invoice invoice = new Invoice();
        invoice.setId(111);
        invoice.setInvoiceDate(LocalDate.now().minusDays(8));
        invoice.setAmount(new BigDecimal("300.00"));
        invoice.setInvoiceStatus("new");
        invoice.setDueDate(LocalDate.now().minusDays(6));

        Invoice invoice2 = new Invoice();
        invoice2.setId(222);
        invoice2.setInvoiceDate(LocalDate.now().plusDays(1));
        invoice2.setAmount(new BigDecimal("200.00"));
        invoice2.setInvoiceStatus("someStatus");

        invoiceDb.add(invoice);
        invoiceDb.add(invoice2);

        return invoiceDb;
    }
}
