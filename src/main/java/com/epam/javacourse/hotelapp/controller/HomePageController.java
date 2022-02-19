package com.epam.javacourse.hotelapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {

    @GetMapping("/home")
    public String getHomePage() {
        return "homePage/home";
    }
}
