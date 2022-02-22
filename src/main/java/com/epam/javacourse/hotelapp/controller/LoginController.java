package com.epam.javacourse.hotelapp.controller;

import com.epam.javacourse.hotelapp.dto.ApplicationDto;
import com.epam.javacourse.hotelapp.dto.UserDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.service.impl.UserServiceImpl;
import com.epam.javacourse.hotelapp.utils.mappers.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.epam.javacourse.hotelapp.utils.Constants.*;
import static java.util.Objects.nonNull;

@Controller
public class LoginController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String getLogin() {
        return PAGE_LOGIN;
    }

    @PostMapping("/login")
    public RedirectView postLogin(HttpServletRequest request, Model model) throws AppException {
        final String email = request.getParameter(PARAM_EMAIL);
        final String password = request.getParameter(PARAM_PASSWORD);

        final HttpSession session = request.getSession();

        if (nonNull(session) &&
                nonNull(session.getAttribute("email")) &&
                nonNull(session.getAttribute("password"))) {

            return new RedirectView(PAGE_HOME);
        }

        User user = userService.getUserByEmail(email);

        UserDto userDto = new UserDto();
        String inputPass = null;
        if (user != null) {

            try {
                inputPass = passwordEncoder.encode(password);
            } catch (Exception e) {
                logger.error("Cannot encode input password ", e);
            }

            UserMapper userMapper = new UserMapper();
            userDto = userMapper.mapToDto(user);
        }
        if (inputPass.equals(user.getPassword())) {
            request.getSession().setAttribute("authorisedUser", true);
            request.getSession().setAttribute("user", userDto);
            request.getSession().setAttribute("email", userDto.getEmail());
            request.getSession().setAttribute("password", userDto.getPassword());
            request.getSession().setAttribute("role", userDto.getRole());
            return new RedirectView(PAGE_HOME);
        } else {
            logger.info("User entered wrong email or password");
            model.addAttribute("wrongInput", true);
            return new RedirectView(PAGE_LOGIN);
        }
    }
}
