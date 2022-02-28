package com.epam.javacourse.hotelapp.controller;

import com.epam.javacourse.hotelapp.dto.*;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.Application;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.service.interfaces.IApplicationService;
import com.epam.javacourse.hotelapp.service.interfaces.IBookingService;
import com.epam.javacourse.hotelapp.service.interfaces.IConfirmationRequest;
import com.epam.javacourse.hotelapp.service.interfaces.IInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

import static com.epam.javacourse.hotelapp.utils.Constants.PAGE_REGISTRATION;
import static com.epam.javacourse.hotelapp.utils.Constants.PAGE_SUBMIT_APPLICATION;

@Controller
@RequestMapping(value = "/client")
public class ClientAccountController {


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

        ModelAndView modelAndView = new ModelAndView("clientAccount");

        modelAndView.addObject("myApplications", userApplications);
        modelAndView.addObject("myBookings", userBookings);
        modelAndView.addObject("myConfirmRequests", userConfirmRequests);
        modelAndView.addObject("myInvoices", userInvoices);

        return modelAndView;
    }

    @GetMapping("/application")
    public String applicationPage(  @ModelAttribute("application") @Valid ApplicationDto applicationDto) {
        return PAGE_SUBMIT_APPLICATION;
    }

    @PostMapping(value = "/application")
    public ModelAndView submitApplication(HttpServletRequest request,
                                          @ModelAttribute("application") @Valid ApplicationDto applicationDto,
                                          BindingResult bindingResult, Model model) {

//        HttpSession session = request.getSession();
//        User authorisedUser = (User) session.getAttribute("authorisedUser");
//
//        String roomTypeBySeats = request.getParameter("room_seats");
//        String roomClass = request.getParameter("room_class");
//
//        String address = Path.PAGE_ERROR;
//
//        String checkinDate = request.getParameter("checkin_date");
//        String checkoutDate = request.getParameter("checkout_date");
//
//        LocalDate checkin = Validator.dateParameterToLocalDate(checkinDate, request);
//        LocalDate checkout = Validator.dateParameterToLocalDate(checkoutDate, request);
//
//        if (!Validator.isCorrectDate(checkin, checkout, request)) {
//            return new AddressCommandResult(address);
//        }
//
//        Application newApplication = new Application();
//        newApplication.setUserId(authorisedUser.getId());
//        newApplication.setRoomTypeBySeats(roomTypeBySeats);
//        newApplication.setRoomClass(roomClass);
//        newApplication.setCheckinDate(checkin);
//        newApplication.setCheckoutDate(checkout);
//
//        applicationService.create(newApplication);
//
        return new ModelAndView("clientAccount");
    }

    @PostMapping(value = "/book")
    public ModelAndView bookRoom(HttpSession session) {



        return new ModelAndView("clientAccount");
    }


}


