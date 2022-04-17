package com.epam.javacourse.hotelapp.controller;

import com.epam.javacourse.hotelapp.dto.UserDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import static com.epam.javacourse.hotelapp.utils.Constants.AUTH_USER;
import static com.epam.javacourse.hotelapp.utils.Constants.PAGE_LOGIN;

@Controller
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/user")
    public ModelAndView redirectUserToAccount(HttpSession session) throws AppException {

        UserDto user = null;
        String role = null;
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                String email = ((UserDetails) principal).getUsername();
                user = userService.getUserByEmail(email);
                role = user.getRole();
            }
        } catch (Exception exception) {
            String errorMessage = "Can't authenticate user";
            throw new AppException(errorMessage, exception);
        }


        if (role == null || role.isEmpty()) {
            return new ModelAndView(PAGE_LOGIN);
        }

        session.setAttribute(AUTH_USER, user);
        session.setAttribute("userRole", role);

        return ("client".equalsIgnoreCase(role)) ? new ModelAndView("redirect:/client/account") :
                new ModelAndView("redirect:/manager1/account");
    }
}