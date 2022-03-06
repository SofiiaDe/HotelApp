package com.epam.javacourse.hotelapp.controller;

import com.epam.javacourse.hotelapp.dto.*;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.Room;
import com.epam.javacourse.hotelapp.service.interfaces.*;
import com.epam.javacourse.hotelapp.utils.mappers.UserMapper;
import com.epam.javacourse.hotelapp.utils.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.epam.javacourse.hotelapp.utils.Constants.*;

@Controller
@RequestMapping(value = "/client")
public class ClientAccountController {

    private static final Logger logger = LogManager.getLogger(ClientAccountController.class);


    private final IClaimService claimService;
    private final IBookingService bookingService;
    private final IConfirmationRequest confirmRequestService;
    private final IInvoiceService invoiceService;
    private final IRoomService roomService;
    private final IBookingInvoiceService bookingInvoiceService;

    public ClientAccountController(IClaimService claimService, IBookingService bookingService,
                                   IConfirmationRequest confirmRequestService, IInvoiceService invoiceService,
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

//        ModelAndView modelAndView = new ModelAndView("clientAccount");
        ModelAndView modelAndView = new ModelAndView(PAGE_CLIENT_ACCOUNT);

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

        return REDIRECT_CLIENT_ACCOUNT;
    }

//    @GetMapping("/book")
//    public String viewFreeRooms(Model model) {
//        return findPaginated(1, "roomClass", "desc", model);
//    }

    @GetMapping("/showNewRoomForm")
    public String showNewRoomForm(Model model) {
        // create model attribute to bind form data
        Room room = new Room();
        model.addAttribute("room", room);
        return "new_room";
    }

    @PostMapping("/saveRoom")
    public String saveEmployee(@ModelAttribute("room") Room room) {
        // save room to database
        roomService.saveRoom(room);
        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") int id, Model model) throws AppException {

        // get room from the service
        Room room = roomService.getRoomById(id);

        // set room as a model attribute to pre-populate the form
        model.addAttribute("room", room);
        return "update_room";
    }

    @GetMapping("/deleteRoom/{id}")
    public String deleteEmployee(@PathVariable(value = "id") int id) {

        // call delete room method
        this.roomService.deleteRoomById(id);
        return "redirect:/";
    }


    @GetMapping("/book")
    public String findPaginated(//@PathVariable(value = "pageNo")
                                @RequestParam(value = "page", required = false)
                                        Integer page,
                                @RequestParam(value = "sortBy", required = false)
                                        String sortByParam,
                                @RequestParam(value = "sortType", required = false)
                                        String sortTypeParam,
                                @RequestParam(value = "status", required = false)
                                        String roomStatus,
                                @RequestParam(value = "seats", required = false)
                                        String roomSeats,
                                @RequestParam(value = "checkin", required = false)
                                        String checkinDate,
                                @RequestParam(value = "checkout", required = false)
                                            String checkoutDate,
                                Model model) throws AppException {
        int pageSize = 5;

        if (page == null || page <= 0) {
            page = 1;
        }

//        List<Room> freeRooms = page.getContent();
        List<Room> freeRooms;
        int totalFreeRooms;
        int totalPageCount = 0;
        Sort sortBy;
        Sort sortType;

        if(checkinDate == null || checkoutDate == null) {
            freeRooms = Collections.emptyList();
        } else {
            LocalDate checkin = Validator.dateParameterToLocalDate(checkinDate);
            LocalDate checkout = Validator.dateParameterToLocalDate(checkoutDate);

            if(sortByParam == null) {
                sortBy = Sort.unsorted();
            } else {
                sortBy = Objects.equals(sortByParam, "class") ? Sort.by("roomClass") : Sort.by("price");
            }
            if(sortTypeParam == null) {
                sortType = Sort.unsorted();
            } else {
                sortType = Objects.equals(sortTypeParam, "asc") ? sortBy.ascending() : sortBy.descending();
            }

            freeRooms = roomService.getAvailablePageableRoomsForPeriod(checkin, checkout, pageSize, page, sortType);
            totalFreeRooms = roomService.getRoomsNumberForPeriod(checkin, checkout);
            totalPageCount = (int) Math.ceil((float)totalFreeRooms / pageSize);

        }
//        List<Room> page = roomService.findPaginated(pageNo, pageSize, sortBy, sortType, roomStatus, roomSeats);

        // pagination parameters
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPageCount", totalPageCount);

        // sorting parameters
//        model.addAttribute("sortBy", sortBy);
//        model.addAttribute("sortType", sortType);
//        model.addAttribute("status", roomStatus);
//        model.addAttribute("seats", roomSeats);
        model.addAttribute("checkin", checkinDate);
        model.addAttribute("checkout", checkoutDate);

        // free rooms
        model.addAttribute("freeRooms", freeRooms);
        return FREE_ROOMS_TO_BOOK;
    }



    @PostMapping(value = "/book")
    public String bookRoom(HttpServletRequest request,
                                 @ModelAttribute("booking") @Valid BookingDto bookingDto,
                                 BindingResult bindingResult) throws AppException {

        if (bindingResult.hasErrors()) {
            return FREE_ROOMS_TO_BOOK;
        }

        HttpSession session = request.getSession();
        UserDto authorisedUser = (UserDto) session.getAttribute("authorisedUser");

        LocalDate checkinDate = bookingDto.getCheckinDate();
        LocalDate checkoutDate = bookingDto.getCheckoutDate();

        if (checkinDate == null || checkoutDate == null) {
            return FREE_ROOMS_TO_BOOK;
        }

        if (!Validator.isCorrectDate(checkinDate, checkoutDate, request)) {
            throw new AppException("Incorrect dates");
        }

        int roomId = bookingDto.getRoomId();
//        int roomId = Integer.parseInt(request.getParameter("room_id"));

        bookingDto.setUserId(authorisedUser.getId());
        bookingDto.setUser(UserMapper.mapFromDto(authorisedUser));
        bookingDto.setClaimId(0);

//        bookingService.createBooking(bookingDto);
//        logger.info("Create booking with id = {}", bookingDto.getId());



        InvoiceDto newInvoice = new InvoiceDto();
        newInvoice.setUserId(bookingDto.getUserId());
        newInvoice.setAmount(bookingInvoiceService.getInvoiceAmount(bookingDto));
        newInvoice.setBookingId(bookingDto.getId());
        newInvoice.setInvoiceDate(LocalDate.now());
        newInvoice.setStatus("new");

        bookingInvoiceService.createBookingAndInvoice(bookingDto, newInvoice);

        // "Thank you! The room was successfully booked.
        // Please check the invoice in your personal account."


        return REDIRECT_CLIENT_ACCOUNT;
    }

    @PostMapping(value = "/payInvoice")
    public String payInvoice(HttpServletRequest request,
                              @ModelAttribute("invoice") @Valid InvoiceDto invoiceDto,
                              BindingResult bindingResult) throws AppException {

        return REDIRECT_CLIENT_ACCOUNT;

    }
}


