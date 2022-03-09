package com.epam.javacourse.hotelapp.controller;

import com.epam.javacourse.hotelapp.dto.*;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.Claim;
import com.epam.javacourse.hotelapp.model.ConfirmationRequest;
import com.epam.javacourse.hotelapp.model.Room;
import com.epam.javacourse.hotelapp.service.interfaces.*;
import com.epam.javacourse.hotelapp.utils.enums.BookingStatus;
import com.epam.javacourse.hotelapp.utils.mappers.BookingMapper;
import com.epam.javacourse.hotelapp.utils.mappers.ClaimMapper;
import com.epam.javacourse.hotelapp.utils.mappers.RoomMapper;
import com.epam.javacourse.hotelapp.utils.mappers.UserMapper;
import com.epam.javacourse.hotelapp.utils.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.epam.javacourse.hotelapp.utils.Constants.*;

@Controller
@RequestMapping(value = "/manager1")
public class ManagerAccountController {

    private static final Logger logger = LogManager.getLogger(ManagerAccountController.class);

    private final IClaimService claimService;
    private final IBookingService bookingService;
    private final IConfirmationRequest confirmRequestService;
    private final IInvoiceService invoiceService;
    private final IRoomService roomService;
    private final IUserService userService;

    public ManagerAccountController(IClaimService claimService, IBookingService bookingService,
                                    IConfirmationRequest confirmRequestService, IInvoiceService invoiceService,
                                    IRoomService roomService, IUserService userService) {
        this.claimService = claimService;
        this.bookingService = bookingService;
        this.confirmRequestService = confirmRequestService;
        this.invoiceService = invoiceService;
        this.roomService = roomService;
        this.userService = userService;
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

    @PostMapping("/makeConfirmRequest")
    public String makeRequest(HttpServletRequest request,
                              @ModelAttribute("claim") @Valid ClaimDto claimDto,
                              BindingResult bindingResult) throws AppException {

        HttpSession session = request.getSession();

        if (!"manager".equalsIgnoreCase((String) session.getAttribute("userRole"))) {
            logger.error("You do not have permission to create a confirmation request. " +
                    "Please login as a Manager.");
            return PAGE_LOGIN;
        }

        if (bindingResult.hasErrors())
            return REDIRECT_MANAGER_ACCOUNT;

        String freeRoomAttrName = "freeRooms";

        var checkin = claimDto.getCheckinDate();
        var checkout = claimDto.getCheckoutDate();

        if (!Validator.isCorrectDate(checkin, checkout, request)) {
            throw new AppException("Incorrect dates");
        }

        if (checkin == null || checkout == null) {
            session.removeAttribute(freeRoomAttrName);
            return REDIRECT_MANAGER_ACCOUNT;
        }

        List<RoomDto> freeRoomsRequest = new ArrayList<>();

        try {
            List<Room> freeRooms = roomService.getFreeRoomsForPeriod(checkin, checkout);
            freeRooms.forEach(x -> freeRoomsRequest.add(RoomMapper.mapToDto(x)));
        } catch (AppException exception) {
            String errorMessage = "Can't retrieve free rooms for period";
            logger.error(errorMessage, exception);
            request.setAttribute("errorMessage", "Can't retrieve rooms data");
            return PAGE_ERROR;
        }

        String notification;

        if (freeRoomsRequest.isEmpty()) {
            notification = "There are no free rooms.";
            request.setAttribute("notification", notification);
            return PAGE_MANAGER_ACCOUNT;
        }

        ClaimDto claimToBeRequested = claimService.getClaimById(claimDto.getId());
        RoomDto suitableRoom = roomService.chooseSuitableRoomForRequest(claimDto, freeRoomsRequest);
        var user = UserMapper.mapFromDto(userService.getUserById(claimToBeRequested.getUserId()));

        ConfirmationRequestDto newConfirmationRequest = new ConfirmationRequestDto();
        newConfirmationRequest.setUserId(user.getId());
        newConfirmationRequest.setClaimId(claimToBeRequested.getId());
        newConfirmationRequest.setRoomId(suitableRoom.getId());
        newConfirmationRequest.setConfirmRequestDate(LocalDate.now());
        newConfirmationRequest.setStatus("new");
        newConfirmationRequest.setUser(user);
        newConfirmationRequest.setClaim(ClaimMapper.mapFromDto(claimToBeRequested));
        newConfirmationRequest.setRoom(RoomMapper.mapFromDto(suitableRoom));

        confirmRequestService.createConfirmRequest(newConfirmationRequest);

        return REDIRECT_MANAGER_ACCOUNT;
    }

}
