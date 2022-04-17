package com.epam.javacourse.hotelapp.utils;

public class Constants {

    private Constants() {
    }

    public static final String PAGE_GET_USERS = "getAllUsers";
    public static final String PAGE_ERROR = "error";
    public static final String PAGE_LOGIN = "login";
    public static final String PAGE_HOME = "home";
    public static final String PAGE_REGISTRATION = "register";
    public static final String PAGE_CLIENT_ACCOUNT = "clientAccount";
    public static final String PAGE_MANAGER_ACCOUNT = "managerAccount";
    public static final String PAGE_SUBMIT_CLAIM = "claim";
    public static final String PAGE_PAY_INVOICE = "paymentTransaction";
    public static final String PAGE_FREE_ROOMS = "freeRoomsToBook";

    public static final String REDIRECT_CLIENT_ACCOUNT = "redirect:/client/account";
    public static final String REDIRECT_MANAGER_ACCOUNT = "redirect:/manager1/account";

    public static final String PROCEDURE_AVAILABLE_ROOMS = "getAvailableRooms";
    public static final String PROCEDURE_UNAVAILABLE_ROOMS = "getUnavailableRooms";
    public static final String PROCEDURE_RESERVED_ROOMS = "getReservedRooms";
    public static final String PROCEDURE_BOOKED_ROOMS = "getBookedRooms";

    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()-\\[{}\\]:;'.,?\\*~$^+=<>]).{8,20}$";
    public static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public static final String AUTH_USER = "authorisedUser";

}
