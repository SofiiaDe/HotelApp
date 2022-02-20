package com.epam.javacourse.hotelapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LocaleController {

    @Autowired
    MessageSource messageSource;

    @GetMapping(value = "/internalization")
    public String getLanguage(@RequestParam(name = "lang") String lang, HttpSession session, HttpServletRequest request) {
        session.setAttribute("locale", lang);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
