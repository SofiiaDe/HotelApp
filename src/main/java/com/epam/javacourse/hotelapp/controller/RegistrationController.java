package com.epam.javacourse.hotelapp.controller;

import com.epam.javacourse.hotelapp.dto.UserDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.UserAlreadyExistsException;
import com.epam.javacourse.hotelapp.service.interfaces.IUserService;
import com.epam.javacourse.hotelapp.utils.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import java.util.Objects;

import static com.epam.javacourse.hotelapp.utils.Constants.PAGE_REGISTRATION;

@Controller
public class RegistrationController {

    private static final Logger logger = LogManager.getLogger(RegistrationController.class);

    public RegistrationController() {
    }

    @Autowired
    IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String newPerson(@ModelAttribute("user") UserDto userDto) {
        return PAGE_REGISTRATION;
    }

    @PostMapping("/register")
    public String create(HttpServletRequest request, @ModelAttribute("user") @Valid UserDto userDto,
                         BindingResult bindingResult, Model model) throws AppException {

        if (bindingResult.hasErrors())
            return PAGE_REGISTRATION;

        String password = userDto.getPassword();
        String confirmPassword = userDto.getConfirmPassword();
        String email = userDto.getEmail();
        String firstName = userDto.getFirstName();
        String lastName = userDto.getLastName();
        String country = userDto.getCountry();
        String role = userDto.getRole().trim().toLowerCase();

        if(!Objects.equals(password, confirmPassword)) {
            bindingResult.rejectValue("confirmPassword", confirmPassword, "Passwords should match");
        }

        if (email == null || password == null ||
                firstName == null || lastName == null ||
                country == null || role == null) {
            return PAGE_REGISTRATION;
        }

        if (!Validator.isEmailValid(email, 50)) {
            throw new AppException("Invalid email");
        }

        if (!Validator.isPasswordValid(password, 2, 8)) {
            throw new AppException("Invalid password");
        }

        String passHash = passwordEncoder.encode(password);
        userDto.setPassword(passHash);

        boolean isUserCreated = false;
        try {
            userService.createUser(userDto);
            isUserCreated = true;
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("isUserExists", true);
            logger.warn("Such user already exists", e);
        }

        String result;
        if (isUserCreated) {
            request.getSession().setAttribute("newUser", userDto);
            logger.info("Create user with id = {}", userDto.getId());
            result = "redirect:/login?success";
        } else {
            result = PAGE_REGISTRATION;
        }
        return result;
    }

}