package com.epam.javacourse.hotelapp.utils;

public class Constants {

    private Constants() {
    }

    public static final String PAGE_GET_USERS = "/WEB-INF/jsp/manager/getAllUsers.jsp";
    public static final String PAGE_ERROR = "/WEB-INF/jsp/errorPage.jsp";
    public static final String PAGE_LOGIN = "login";
    public static final String PAGE_HOME = "home";
    public static final String PAGE_REGISTRATION = "register";
    public static final String PAGE_CLIENT_ACCOUNT = "clientAccount";
    public static final String PAGE_MANAGER_ACCOUNT = "managerAccount";
    public static final String PAGE_SUBMIT_APPLICATION = "/WEB-INF/jsp/client/application.jsp";
    public static final String PAGE_PAY_INVOICE = "/WEB-INF/jsp/client/paymentTransaction.jsp";
    public static final String PAGE_FREE_ROOMS = "/WEB-INF/jsp/client/freeRoomsToBook.jsp";

    public static final String PARAM_PAGES = "pages";
    public static final String PARAM_GENDER = "gender";
    public static final String PARAM_ACTIVITY = "activity";
    public static final String PARAM_NUTRITION_GOAL = "nutr_goal";
    public static final String PARAM_PUBLIC = "public";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_USER = "user";
    public static final String PARAM_WEEK_INFO = "weekInfo";
    public static final String PARAM_FOOD = "food";
    public static final String PARAM_EAT_PERIOD = "eat_period";

    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–\\[{}\\]:;',?\\*~$^+=<>]).{8,20}$";
    public static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

}
