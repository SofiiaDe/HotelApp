package com.epam.javacourse.hotelapp.controller;

import com.epam.javacourse.hotelapp.dto.*;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.service.interfaces.IApplicationService;
import com.epam.javacourse.hotelapp.service.interfaces.IBookingService;
import com.epam.javacourse.hotelapp.service.interfaces.IConfirmationRequest;
import com.epam.javacourse.hotelapp.service.interfaces.IInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping(value = "/client")
public class ClientAccountController {

//    @Autowired
//    private IApplicationService applicationService;
//
//    @Autowired
//    private IBookingService bookingService;
//
//    @Autowired
//    private IConfirmationRequest confirmRequestService;
//
//    @Autowired
//    private IInvoiceService invoiceService;

    private final IApplicationService applicationService;
    private final IBookingService bookingService;
    private final IConfirmationRequest confirmRequestService;
    private final IInvoiceService invoiceService;

    public ClientAccountController(IApplicationService applicationService, IBookingService bookingService,
                                   IConfirmationRequest confirmRequestService, IInvoiceService invoiceService) {
        this.applicationService = applicationService;
        this.bookingService = bookingService;
        this.confirmRequestService = confirmRequestService;
        this.invoiceService = invoiceService;
    }

    @GetMapping(value = "/account")
    public ModelAndView getClientAccount(HttpSession session) throws AppException {

        ModelAndView modelAndView = new ModelAndView("redirect:/client/account");
        UserDto authorisedUser = (UserDto) session.getAttribute("authorisedUser");

        List<ApplicationClientDto> userApplications = applicationService.getUserDetailedApplications(authorisedUser.getId());
        userApplications.sort(Comparator.comparing(ApplicationClientDto::getCheckinDate).reversed());

//        int userBookingsCount = bookingService.getUserBookingsCount(authorisedUser.getId());
//        int pageCount = (int) Math.ceil((float) userBookingsCount / pageSize);
//
//        boolean toGetBookings = userBookingsCount > 0 && page <= pageCount;
//        List<UserBookingDetailed> userBookings = toGetBookings ?
//                bookingService.getUserDetailedBookings(authorisedUser.getId(), page, pageSize) : new ArrayList<>();

        List<BookingClientDto> userBookings = bookingService.getUserDetailedBookings(authorisedUser.getId());
        userBookings.sort(Comparator.comparing(BookingClientDto::getCheckinDate).reversed());

        List<ConfirmationRequestClientDto> userConfirmRequests = confirmRequestService
                .getUserDetailedConfirmRequests(authorisedUser.getId());
        userConfirmRequests.sort(Comparator.comparing(ConfirmationRequestClientDto::getConfirmRequestDate).reversed());

        List<InvoiceClientDto> userInvoices = invoiceService.getUserDetailedInvoices(authorisedUser.getId());
        userInvoices.sort(Comparator.comparing(InvoiceClientDto::getInvoiceDate));


        modelAndView.addObject("myApplications", userApplications);
        modelAndView.addObject("myBookings", userBookings);
        modelAndView.addObject("myConfirmRequests", userConfirmRequests);
        modelAndView.addObject("myInvoices", userInvoices);

        return modelAndView;
    }

    @PostMapping(value = "/book")
    ModelAndView bookRoom() {

        return new ModelAndView("redirect:/client/account");
    }



}


