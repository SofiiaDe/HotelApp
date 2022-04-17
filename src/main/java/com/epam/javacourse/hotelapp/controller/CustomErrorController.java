package com.epam.javacourse.hotelapp.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static com.epam.javacourse.hotelapp.utils.Constants.PAGE_ERROR;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger logger = LogManager.getLogger(CustomErrorController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            logger.info("Error: error code {}", statusCode);
        }

        return PAGE_ERROR;
    }

}
