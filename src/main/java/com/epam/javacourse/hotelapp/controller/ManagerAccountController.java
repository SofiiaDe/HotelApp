package com.epam.javacourse.hotelapp.controller;

import com.epam.javacourse.hotelapp.dto.*;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.Room;
import com.epam.javacourse.hotelapp.service.interfaces.*;
import com.epam.javacourse.hotelapp.utils.enums.BookingStatus;
import com.epam.javacourse.hotelapp.utils.mappers.ClaimMapper;
import com.epam.javacourse.hotelapp.utils.mappers.RoomMapper;
import com.epam.javacourse.hotelapp.utils.mappers.UserMapper;
import com.epam.javacourse.hotelapp.utils.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final IConfirmationRequestService confirmRequestService;
    private final IInvoiceService invoiceService;
    private final IRoomService roomService;
    private final IUserService userService;

    @Autowired
    public ManagerAccountController(IClaimService claimService, IBookingService bookingService,
                                    IConfirmationRequestService confirmRequestService, IInvoiceService invoiceService,
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

        List<Room> freeRooms = roomService.getFreeRoomsForPeriod(checkin, checkout);
        freeRooms.forEach(x -> freeRoomsRequest.add(RoomMapper.mapToDto(x)));

        if (freeRoomsRequest.isEmpty()) {
            String notification = "There are no free rooms.";
            logger.info(notification);
            request.setAttribute("error", notification);
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

    @PostMapping("/removeClaim/{id}")
    public String removeClaim(@PathVariable(value = "id") int id) {
        claimService.removeClaim(id);
        return REDIRECT_MANAGER_ACCOUNT;
    }

    @GetMapping(value = "/allUsersList")
    public ModelAndView getAllUsersList() throws AppException {

        List<UserDto> usersList = userService.getAllUsers();
        ModelAndView modelAndView = new ModelAndView(PAGE_GET_USERS);

        modelAndView.addObject("usersList", usersList);

        return modelAndView;
    }

    @PostMapping("/addRoom")
    public String addRoom(@Valid RoomDto newRoom,
                          BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "redirect:/manager1/addRoom";

        roomService.createRoom(newRoom);

        return REDIRECT_MANAGER_ACCOUNT;
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") int id, Model model) throws AppException {

        // get room from the service
        RoomDto room = roomService.getRoomById(id);

        // set room as a model attribute to pre-populate the form
        model.addAttribute("room", room);
        return "updateRoom";
    }

    @GetMapping("/deleteRoom/{id}")
    public String deleteRoom(@PathVariable(value = "id") int id) {

        this.roomService.deleteRoomById(id);
        return "redirect:/";
    }


}
