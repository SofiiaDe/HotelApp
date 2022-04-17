package com.epam.javacourse.hotelapp.controller;

import com.epam.javacourse.hotelapp.dto.*;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.Room;
import com.epam.javacourse.hotelapp.service.interfaces.*;
import com.epam.javacourse.hotelapp.utils.mappers.BookingMapper;
import com.epam.javacourse.hotelapp.utils.mappers.UserMapper;
import com.epam.javacourse.hotelapp.utils.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.epam.javacourse.hotelapp.utils.Constants.*;

@Controller
@RequestMapping(value = "/client")
public class ClientAccountController {

    private static final Logger logger = LogManager.getLogger(ClientAccountController.class);

    private final IClaimService claimService;
    private final IBookingService bookingService;
    private final IConfirmationRequestService confirmRequestService;
    private final IInvoiceService invoiceService;
    private final IRoomService roomService;
    private final IBookingInvoiceService bookingInvoiceService;

    @Autowired
    public ClientAccountController(IClaimService claimService, IBookingService bookingService,
                                   IConfirmationRequestService confirmRequestService, IInvoiceService invoiceService,
                                   IRoomService roomService, IBookingInvoiceService bookingInvoiceService) {
        this.claimService = claimService;
        this.bookingService = bookingService;
        this.confirmRequestService = confirmRequestService;
        this.invoiceService = invoiceService;
        this.roomService = roomService;
        this.bookingInvoiceService = bookingInvoiceService;
    }

    @GetMapping(value = "/account")
    public ModelAndView getClientAccount(HttpSession session) throws AppException {

        UserDto authorisedUser = (UserDto) session.getAttribute(AUTH_USER);

        List<ClaimClientDto> userClaims = claimService.getUserDetailedClaims(authorisedUser.getId());
        userClaims.sort(Comparator.comparing(ClaimClientDto::getCheckinDate).reversed());

        List<BookingClientDto> userBookings = bookingService.getUserDetailedBookings(authorisedUser.getId());
        userBookings.sort(Comparator.comparing(BookingClientDto::getCheckinDate).reversed());

        List<ConfirmationRequestClientDto> userConfirmRequests = confirmRequestService
                .getUserDetailedConfirmRequests(authorisedUser.getId());
        userConfirmRequests.sort(Comparator.comparing(ConfirmationRequestClientDto::getConfirmRequestDate).reversed());

        List<InvoiceClientDto> userInvoices = invoiceService.getUserDetailedInvoices(authorisedUser.getId());
        userInvoices.sort(Comparator.comparing(InvoiceClientDto::getInvoiceDate));

        ModelAndView modelAndView = new ModelAndView(PAGE_CLIENT_ACCOUNT);

        modelAndView.addObject("myClaims", userClaims);
        modelAndView.addObject("myBookings", userBookings);
        modelAndView.addObject("myConfirmRequests", userConfirmRequests);
        modelAndView.addObject("myInvoices", userInvoices);

        return modelAndView;
    }


    @GetMapping("/claim")
    public String claim(@ModelAttribute("claim") ClaimDto claimDto) {
        return PAGE_SUBMIT_CLAIM;
    }

    @PostMapping(value = "/claim")
    public String submitClaim(HttpServletRequest request,
                              @ModelAttribute("claim") @Valid ClaimDto claimDto,
                              BindingResult bindingResult) throws AppException {

        if (bindingResult.hasErrors()) {
            return PAGE_SUBMIT_CLAIM;
        }
        HttpSession session = request.getSession();
        UserDto authorisedUser = (UserDto) session.getAttribute(AUTH_USER);

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

        return REDIRECT_CLIENT_ACCOUNT;
    }


    @GetMapping("/book")
    public String findPaginated(@ModelAttribute("booking") BookingDto bookingDto,
                                @RequestParam(value = "page", required = false)
                                        Integer page,
                                @RequestParam(value = "sortBy", required = false)
                                        String sortByParam,
                                @RequestParam(value = "sortType", required = false)
                                        String sortTypeParam,
                                @RequestParam(value = "status", required = false)
                                        String roomStatusParam,
                                @RequestParam(value = "seats", required = false)
                                        String roomSeatsParam,
                                @RequestParam(value = "checkin", required = false)
                                @Future(message = "{checkin.future}")
                                @DateTimeFormat(pattern = "yyyy-MM-dd")
                                        String checkinDate,
                                @RequestParam(value = "checkout", required = false)
                                @Future(message = "{checkout.future}")
                                @DateTimeFormat(pattern = "yyyy-MM-dd")
                                        String checkoutDate,
                                BindingResult bindingResult,
                                Model model) throws AppException {
        if (bindingResult.hasErrors()) {
            return PAGE_FREE_ROOMS;
        }

        int pageSize = 5;

        if (page == null || page <= 0) {
            page = 1;
        }

        List<Room> freeRooms;
        int totalFreeRooms;
        int totalPageCount = 0;

        if (checkinDate == null || checkoutDate == null) {
            freeRooms = Collections.emptyList();

        } else {

            LocalDate checkin = Validator.dateParameterToLocalDate(checkinDate);
            LocalDate checkout = Validator.dateParameterToLocalDate(checkoutDate);

            totalFreeRooms = roomService.getRoomsNumberForPeriod(checkin, checkout);
            totalPageCount = (int) Math.ceil((float) totalFreeRooms / pageSize);

            boolean toGetRooms = totalFreeRooms > 0 && page <= totalPageCount;

            freeRooms = toGetRooms ?
                    roomService.getRoomsForPeriod(checkin, checkout, pageSize, page, sortByParam, sortTypeParam, roomSeatsParam, roomStatusParam) :
                    new ArrayList<>();
        }

        // pagination parameters
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPageCount", totalPageCount);

        // sorting parameters
        model.addAttribute("sortBy", sortByParam);
        model.addAttribute("sortType", sortTypeParam);
        model.addAttribute("status", roomStatusParam);
        model.addAttribute("seats", roomSeatsParam);
        model.addAttribute("checkin", checkinDate);
        model.addAttribute("checkout", checkoutDate);

        // free rooms
        model.addAttribute("freeRooms", freeRooms);
        return PAGE_FREE_ROOMS;
    }

    @PostMapping(value = "/book")
    public String bookRoom(HttpServletRequest request,
                           @ModelAttribute("booking") @Valid BookingDto bookingDto,
                           BindingResult bindingResult) throws AppException {

        if (bindingResult.hasErrors()) {
            return PAGE_FREE_ROOMS;
        }

        HttpSession session = request.getSession();
        UserDto authorisedUser = (UserDto) session.getAttribute(AUTH_USER);

        LocalDate checkinDate = bookingDto.getCheckin();
        LocalDate checkoutDate = bookingDto.getCheckout();

        if (checkinDate == null || checkoutDate == null) {
            return PAGE_FREE_ROOMS;
        }

        if (!Validator.isCorrectDate(checkinDate, checkoutDate, request)) {
            throw new AppException("Incorrect dates");
        }

        bookingDto.setUserId(authorisedUser.getId());
        bookingDto.setUser(UserMapper.mapFromDto(authorisedUser));
        bookingDto.setClaimId(0);

        InvoiceDto newInvoice = new InvoiceDto();
        newInvoice.setUserId(bookingDto.getUserId());
        try {
            newInvoice.setAmount(bookingInvoiceService.getInvoiceAmount(bookingDto));
        } catch (Exception exception) {
            throw new AppException("Can't calculate invoice amount", exception);
        }

        newInvoice.setInvoiceDate(LocalDate.now());
        newInvoice.setStatus("new");
        newInvoice.setBooking(BookingMapper.mapFromDto(bookingDto));
        newInvoice.setUser(UserMapper.mapFromDto(authorisedUser));

        try {
            bookingInvoiceService.createBookingAndInvoice(bookingDto, newInvoice);
        } catch (Exception exception) {
            throw new AppException("Can't create new booking and invoice", exception);
        }

        // "Thank you! The room was successfully booked.
        // Please check the invoice in your personal account."

        return REDIRECT_CLIENT_ACCOUNT;
    }

    @GetMapping("/paymentPage")
    public String payment(@RequestParam(value = "invoiceId") Integer invoiceId,
                          Model model) {

        model.addAttribute("invoiceId", invoiceId);
        return PAGE_PAY_INVOICE;
    }

    /**
     * Provide invoice payment
     */
    @ResponseBody
    @PostMapping(value = "/payInvoice")
    public String payInvoice(@RequestParam(value = "invoiceId") Integer invoiceId) throws AppException {
        try {
            bookingInvoiceService.payInvoice(invoiceId);
        } catch (AppException exception) {
            throw new AppException("Can't pay invoice", exception);
        }
        return REDIRECT_CLIENT_ACCOUNT;
    }

    @PostMapping(value = "/confirmRequest")
    public String confirmRequest(HttpServletRequest request,
                                 @RequestParam("confirmRequestId") Integer confirmRequestId) throws AppException {

        HttpSession session = request.getSession();
        UserDto authorisedUser = (UserDto) session.getAttribute(AUTH_USER);

        confirmRequestService.confirmRequestByClient(confirmRequestId);

        ConfirmationRequestDto confirmRequestToBook = confirmRequestService.getConfirmRequestById(confirmRequestId);
        ClaimDto claimOfConfirmRequest = claimService.getClaimById(confirmRequestToBook.getClaimId());

        // add new booking and new invoice to DB
        BookingDto newBooking = new BookingDto();
        newBooking.setUserId(authorisedUser.getId());
        newBooking.setCheckin(claimOfConfirmRequest.getCheckinDate());
        newBooking.setCheckout(claimOfConfirmRequest.getCheckoutDate());
        newBooking.setRoomId(confirmRequestToBook.getRoomId());
        newBooking.setClaimId(confirmRequestToBook.getClaimId());
        newBooking.setUser(UserMapper.mapFromDto(authorisedUser));

        InvoiceDto newInvoice = new InvoiceDto();
        newInvoice.setUserId(newBooking.getUserId());

        try {
            newInvoice.setAmount(bookingInvoiceService.getInvoiceAmount(newBooking));
        } catch (Exception exception) {
            throw new AppException("Can't calculate invoice amount", exception);
        }

        newInvoice.setBookingId(newBooking.getId());
        newInvoice.setInvoiceDate(LocalDate.now());
        newInvoice.setStatus("new");
        newInvoice.setUser(UserMapper.mapFromDto(authorisedUser));

        try {
            bookingInvoiceService.createBookingAndInvoice(newBooking, newInvoice);
        } catch (Exception exception) {
            throw new AppException("Can't create new booking and invoice", exception);
        }

        return REDIRECT_CLIENT_ACCOUNT;
    }

}


