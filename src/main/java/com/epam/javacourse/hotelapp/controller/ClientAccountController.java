package com.epam.javacourse.hotelapp.controller;

import com.epam.javacourse.hotelapp.dto.*;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.service.interfaces.IClaimService;
import com.epam.javacourse.hotelapp.service.interfaces.IBookingService;
import com.epam.javacourse.hotelapp.service.interfaces.IConfirmationRequest;
import com.epam.javacourse.hotelapp.service.interfaces.IInvoiceService;
import com.epam.javacourse.hotelapp.utils.mappers.UserMapper;
import com.epam.javacourse.hotelapp.utils.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static com.epam.javacourse.hotelapp.utils.Constants.PAGE_SUBMIT_CLAIM;

@Controller
@RequestMapping(value = "/client")
public class ClientAccountController {

    private static final Logger logger = LogManager.getLogger(ClientAccountController.class);


    private final IClaimService claimService;
    private final IBookingService bookingService;
    private final IConfirmationRequest confirmRequestService;
    private final IInvoiceService invoiceService;

    public ClientAccountController(IClaimService claimService, IBookingService bookingService,
                                   IConfirmationRequest confirmRequestService, IInvoiceService invoiceService) {
        this.claimService = claimService;
        this.bookingService = bookingService;
        this.confirmRequestService = confirmRequestService;
        this.invoiceService = invoiceService;
    }

    @GetMapping(value = "/account")
    public ModelAndView getClientAccount(HttpSession session) throws AppException {


        UserDto authorisedUser = (UserDto) session.getAttribute("authorisedUser");

        List<ClaimClientDto> userClaims = claimService.getUserDetailedClaims(authorisedUser.getId());
        userClaims.sort(Comparator.comparing(ClaimClientDto::getCheckinDate).reversed());

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

        modelAndView.addObject("myClaims", userClaims);
        modelAndView.addObject("myBookings", userBookings);
        modelAndView.addObject("myConfirmRequests", userConfirmRequests);
        modelAndView.addObject("myInvoices", userInvoices);

        return modelAndView;
    }


    @GetMapping("/claim")
    public String claim() {
        return PAGE_SUBMIT_CLAIM;
    }

    @PostMapping(value = "/claim")
    public String submitClaim(HttpServletRequest request,
                                    @ModelAttribute("claim") @Valid ClaimDto claimDto,
                                    BindingResult bindingResult) throws AppException {

        if (bindingResult.hasErrors())
            return PAGE_SUBMIT_CLAIM;

        HttpSession session = request.getSession();
        UserDto authorisedUser = (UserDto) session.getAttribute("authorisedUser");

        String roomSeats = claimDto.getRoomSeats();
        String roomClass = claimDto.getRoomClass();

        LocalDate checkinDate = claimDto.getCheckinDate();
        LocalDate checkoutDate = claimDto.getCheckoutDate();

        if (roomSeats == null || roomClass == null ||
                checkinDate == null || checkoutDate == null) {
            return PAGE_SUBMIT_CLAIM;
        }

        if (!Validator.isCorrectDate(checkinDate, checkoutDate, request)) {
            throw new AppException("Incorrect dates");
        }

        claimDto.setUserId(authorisedUser.getId());
        claimDto.setUser(UserMapper.mapFromDto(authorisedUser));

        claimService.createClaim(claimDto);
        logger.info("Create claim with id = {}", claimDto.getId());

        return "redirect:/client/account";
    }

    @PostMapping(value = "/book")
    public ModelAndView bookRoom(HttpSession session) {


        return new ModelAndView("clientAccount");
    }


}


