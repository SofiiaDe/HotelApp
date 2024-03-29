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

    private final MessageSource messageSource;

    @Autowired
    public LocaleController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @GetMapping(value = "/language")
    public String getLanguage(@RequestParam("lang") String lang, HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.setAttribute("locale", lang);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
