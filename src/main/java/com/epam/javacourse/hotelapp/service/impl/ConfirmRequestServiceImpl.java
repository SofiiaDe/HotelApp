package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.dto.*;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.Application;
import com.epam.javacourse.hotelapp.model.ConfirmationRequest;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.*;
import com.epam.javacourse.hotelapp.service.interfaces.IApplicationService;
import com.epam.javacourse.hotelapp.service.interfaces.IConfirmationRequest;
import com.epam.javacourse.hotelapp.service.interfaces.IUserService;
import com.epam.javacourse.hotelapp.utils.mappers.ApplicationMapper;
import com.epam.javacourse.hotelapp.utils.mappers.ConfirmationRequestMapper;
import com.epam.javacourse.hotelapp.utils.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConfirmRequestServiceImpl implements IConfirmationRequest {

    @Autowired
    ConfirmRequestRepository confirmRequestRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ApplicationRepository applicationRepository;


    @Override
    public LocalDate getConfirmRequestDueDate(ConfirmationRequestDto confirmRequest) {
        LocalDate confirmRequestDate = confirmRequest.getConfirmRequestDate();
        return confirmRequestDate.plusDays(2);

    }

    @Override
    public List<ConfirmationRequestManagerDto> getAllDetailedConfirmRequests() throws AppException {
        try {
            List<ConfirmationRequest> allConfirmRequests = confirmRequestRepository.findAll();

            if (allConfirmRequests.isEmpty()) {
                return Collections.emptyList();
            }

            List<Integer> userIds = allConfirmRequests.stream()
                    .map(ConfirmationRequest::getUserId)
                    .map(User::getId)
                    .distinct()
                    .collect(Collectors.toList());

            List<UserDto> userDtos = userRepository.findUsersByIds(userIds)
                    .stream()
                    .map(UserMapper::mapToDto)
                    .collect(Collectors.toList());

            List<ConfirmationRequestManagerDto> result = new ArrayList<>();

            for (ConfirmationRequest confirmRequest : allConfirmRequests) {
                var bookingUser = userDtos.stream().filter(u -> u.getId() == confirmRequest.getUserId().getId()).findFirst().get();
                result.add(
                        new ConfirmationRequestManagerDto(confirmRequest.getId(),
                                bookingUser.getFirstName() + ' ' + bookingUser.getLastName(),
                                bookingUser.getEmail(),
                                confirmRequest.getApplicationId().getId(),
                                confirmRequest.getRoomId().getId(),
                                confirmRequest.getConfirmRequestDate(),
                                confirmRequest.getStatus()
                        ));
            }

            return result;
        } catch (DBException exception) {
            throw new AppException("Can't retrieve all confirmation requests to show in the manager's account", exception);
        }
    }

    @Override
    public List<ConfirmationRequestClientDto> getUserDetailedConfirmRequests(int userID) throws AppException {

        try {

            List<ConfirmationRequest> allUserConfirmRequests = confirmRequestRepository.findConfirmationRequestsByUserId(userID);

            if (allUserConfirmRequests.isEmpty()) {
                return Collections.emptyList();
            }

            List<ApplicationDto> userApplications = applicationRepository.findApplicationsByUserId(userID)
                    .stream()
                    .map(ApplicationMapper::mapToDto)
                    .collect(Collectors.toList());

            List<ConfirmationRequestClientDto> result = new ArrayList<>();

            for (ConfirmationRequest confirmRequest : allUserConfirmRequests) {
                var application = userApplications.stream()
                        .filter(a -> a.getId() == confirmRequest.getApplicationId().getId())
                        .findFirst()
                        .get();
                result.add(
                        new ConfirmationRequestClientDto(confirmRequest.getId(),
                                confirmRequest.getConfirmRequestDate(),
                                getConfirmRequestDueDate(ConfirmationRequestMapper.mapToDto(confirmRequest)),
                                application.getRoomTypeBySeats(),
                                application.getRoomClass(),
                                application.getCheckinDate(),
                                application.getCheckoutDate(),
                                confirmRequest.getApplicationId().getId(),
                                confirmRequest.getStatus()
                        ));
            }
            return result;
        } catch (DBException exception) {
            throw new AppException("Can't retrieve client's confirmation requests to show in the client's account", exception);
        }
    }

}
