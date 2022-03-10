package com.epam.javacourse.hotelapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
public class LocaleController {

    @Autowired
    MessageSource messageSource;

    @GetMapping(value = "/language")
    public String getLanguage(@RequestParam("lang") String lang, HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.setAttribute("locale", lang);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
