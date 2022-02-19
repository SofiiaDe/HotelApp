package com.epam.javacourse.hotelapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.epam.javacourse.hotelapp.controller.Path.PAGE_HOME;

@Controller
public class HomePageController {

    @GetMapping("/home")
    public String getHomePage() {
        return PAGE_HOME;
    }
}
