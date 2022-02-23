package com.epam.javacourse.hotelapp.controller;

import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import static com.epam.javacourse.hotelapp.utils.Constants.*;

@Controller
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping(value = "/user")
    public String redirectUserToAccount() throws AppException {

        User user;
        String role = null;
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                String email = ((UserDetails) principal).getUsername();
                user = userService.getUserByEmail(email);
                role = user.getRole();
            }
        } catch (AppException exception) {
            String errorMessage = "Can't authenticate user";
            throw new AppException(errorMessage, exception);
        }

        String result = null;

        if (role == null || role.isEmpty()) {
            result = PAGE_LOGIN;
        }

        if ("manager" .equalsIgnoreCase(role)) {
            result = PAGE_MANAGER_ACCOUNT;
        } else if ("client" .equalsIgnoreCase(role)) {
            result = PAGE_CLIENT_ACCOUNT;
        }

        return result;
    }
}