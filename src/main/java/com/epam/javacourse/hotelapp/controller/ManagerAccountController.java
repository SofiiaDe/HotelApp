package com.epam.javacourse.hotelapp.controller;

import com.epam.javacourse.hotelapp.dto.*;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.service.interfaces.IClaimService;
import com.epam.javacourse.hotelapp.service.interfaces.IBookingService;
import com.epam.javacourse.hotelapp.service.interfaces.IConfirmationRequest;
import com.epam.javacourse.hotelapp.service.interfaces.IInvoiceService;
import com.epam.javacourse.hotelapp.utils.enums.BookingStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static com.epam.javacourse.hotelapp.utils.Constants.PAGE_MANAGER_ACCOUNT;

@Controller
@RequestMapping(value = "/manager1")
public class ManagerAccountController {


    private final IClaimService claimService;
    private final IBookingService bookingService;
    private final IConfirmationRequest confirmRequestService;
    private final IInvoiceService invoiceService;

    public ManagerAccountController(IClaimService claimService, IBookingService bookingService,
                                    IConfirmationRequest confirmRequestService, IInvoiceService invoiceService) {
        this.claimService = claimService;
        this.bookingService = bookingService;
        this.confirmRequestService = confirmRequestService;
        this.invoiceService = invoiceService;
    }

    @GetMapping(value = "/account")
    public ModelAndView getManagerAccount(HttpSession session, HttpServletRequest request) throws AppException {

        UserDto authorisedUser = (UserDto) session.getAttribute("authorisedUser");

        List<ClaimManagerDto> allClaims = claimService.getAllDetailedClaims();

        BookingStatus bookingStatus = BookingStatus.fromString(request.getParameter("bookingStatus"));
//        int allBookingsCount = bookingService.getAllBookingsCount(bookingStatus);
//        int pageCount = (int) Math.ceil((float)allBookingsCount / pageSize);
//
//        boolean toGetBookings = allBookingsCount > 0 && page <= pageCount;
//        List<BookingManagerDto> allBookings = toGetBookings ?
//                bookingService.getAllDetailedBookings(page, pageSize, bookingStatus) : new ArrayList<>();

        List<BookingManagerDto> allBookings = bookingService.getAllDetailedBookings(bookingStatus);

        List<InvoiceManagerDto> allInvoices = invoiceService.getAllDetailedInvoices();

        List<ConfirmationRequestManagerDto> allConfirmRequests = confirmRequestService.getAllDetailedConfirmRequests();

        ModelAndView modelAndView = new ModelAndView(PAGE_MANAGER_ACCOUNT);

        modelAndView.addObject("allClaims", allClaims);
        modelAndView.addObject("allBookings", allBookings);
        modelAndView.addObject("allConfirmRequests", allConfirmRequests);
        modelAndView.addObject("allInvoices", allInvoices);


//        request.setAttribute("page", page);
//        request.setAttribute("pageCount", pageCount)
        return modelAndView;
    }

}
