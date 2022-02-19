package com.epam.javacourse.hotelapp.controller;

public class Path {

    private Path() {
    }

    public static final String PAGE_GET_USERS = "/WEB-INF/jsp/manager/getAllUsers.jsp";
    public static final String PAGE_ERROR = "/WEB-INF/jsp/errorPage.jsp";
    public static final String PAGE_LOGIN = "/WEB-INF/jsp/login.jsp";
    public static final String PAGE_HOME = "home";
    public static final String PAGE_REGISTRATION = "/WEB-INF/jsp/registerPage.jsp";
    public static final String PAGE_CLIENT_ACCOUNT = "/WEB-INF/jsp/client/clientAccount.jsp";
    public static final String PAGE_MANAGER_ACCOUNT = "/WEB-INF/jsp/manager/managerAccount.jsp";
    public static final String PAGE_SUBMIT_APPLICATION = "/WEB-INF/jsp/client/application.jsp";
    public static final String PAGE_PAY_INVOICE = "/WEB-INF/jsp/client/paymentTransaction.jsp";
    public static final String PAGE_FREE_ROOMS = "/WEB-INF/jsp/client/freeRoomsToBook.jsp";
}
