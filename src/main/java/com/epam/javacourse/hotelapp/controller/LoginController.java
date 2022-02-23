package com.epam.javacourse.hotelapp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.epam.javacourse.hotelapp.utils.Constants.*;


@Controller
public class LoginController {

        @GetMapping("/login")
        public String login(){
            return PAGE_LOGIN;
        }
    }

