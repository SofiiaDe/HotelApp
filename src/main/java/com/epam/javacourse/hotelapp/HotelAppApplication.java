package com.epam.javacourse.hotelapp;

import com.epam.javacourse.hotelapp.controller.ClientAccountController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HotelAppApplication {

    private static final Logger logger = LogManager.getLogger(HotelAppApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(HotelAppApplication.class, args);
        logger.info("Springboot pagination and sorting with thymeleaf application is started successfully.");

    }

}
