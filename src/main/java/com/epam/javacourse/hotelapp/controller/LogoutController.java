package com.epam.javacourse.hotelapp.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.epam.javacourse.hotelapp.utils.Constants.AUTH_USER;
import static com.epam.javacourse.hotelapp.utils.Constants.PAGE_HOME;

@Controller
public class LogoutController {

    private static final Logger logger = LogManager.getLogger(LogoutController.class);

    @GetMapping("/logout")
    public RedirectView postLogout(HttpServletRequest request, SessionStatus sessionStatus) {

        HttpSession session = request.getSession(false);
        session.removeAttribute(AUTH_USER);
        session.removeAttribute("email");
        session.removeAttribute("password");
        session.removeAttribute("role");
        session.setAttribute(AUTH_USER, false);

        session.invalidate();
        logger.debug("Logout finished");

        sessionStatus.setComplete();
        return new RedirectView(PAGE_HOME);
    }
}

