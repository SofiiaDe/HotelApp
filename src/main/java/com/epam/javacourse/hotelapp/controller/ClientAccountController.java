package com.epam.javacourse.hotelapp.controller;

import com.epam.javacourse.hotelapp.dto.ApplicationDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.service.impl.ApplicationServiceImpl;
import com.epam.javacourse.hotelapp.service.interfaces.IApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.epam.javacourse.hotelapp.utils.Constants.PAGE_CLIENT_ACCOUNT;

@Controller
@RequestMapping(value = "/client")
public class ClientAccountController {

    @Autowired
    private IApplicationService applicationService;

    @GetMapping(value = "/edit")
    public String getEditPage(HttpSession session, Model model) throws AppException {

        User authorisedUser = (User) session.getAttribute("authorisedUser");


//        List<ApplicationDto> userApplications = applicationService.getUserDetailedApplications(authorisedUser.getId());
//        userApplications.sort(Comparator.comparing(UserApplicationDetailed::getCheckinDate).reversed());
//
//        int userBookingsCount = bookingService.getUserBookingsCount(authorisedUser.getId());
//        int pageCount = (int) Math.ceil((float) userBookingsCount / pageSize);
//
//        boolean toGetBookings = userBookingsCount > 0 && page <= pageCount;
//        List<UserBookingDetailed> userBookings = toGetBookings ?
//                bookingService.getUserDetailedBookings(authorisedUser.getId(), page, pageSize) : new ArrayList<>();
//        userBookings.sort(Comparator.comparing(UserBookingDetailed::getCheckinDate).reversed());
//
//        List<UserConfirmationRequestDetailed> userConfirmRequests = confirmRequestService
//                .getUserDetailedConfirmRequests(authorisedUser.getId());
//        userConfirmRequests.sort(Comparator.comparing(UserConfirmationRequestDetailed::getConfirmRequestDate).reversed());
//
//        List<UserInvoiceDetailed> userInvoices = invoiceService.getUserDetailedInvoices(authorisedUser.getId());
//        userInvoices.sort(Comparator.comparing(UserInvoiceDetailed::getInvoiceDate));
//
//        session.setAttribute("myApplications", userApplications);
//        session.setAttribute("myBookings", userBookings);
//        session.setAttribute("myConfirmRequests", userConfirmRequests);
//        session.setAttribute("myInvoices", userInvoices);
//        request.setAttribute("page", page);
//        request.setAttribute("pageCount", pageCount);
//    }
        return PAGE_CLIENT_ACCOUNT;
}

}
