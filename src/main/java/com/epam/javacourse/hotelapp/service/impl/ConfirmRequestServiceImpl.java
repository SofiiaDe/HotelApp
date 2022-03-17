package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.dto.*;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.ConfirmationRequest;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.*;
import com.epam.javacourse.hotelapp.service.interfaces.IConfirmationRequestService;
import com.epam.javacourse.hotelapp.utils.mappers.ClaimMapper;
import com.epam.javacourse.hotelapp.utils.mappers.ConfirmationRequestMapper;
import com.epam.javacourse.hotelapp.utils.mappers.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConfirmRequestServiceImpl implements IConfirmationRequestService {

    private static final Logger logger = LogManager.getLogger(ConfirmRequestServiceImpl.class);

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
    ClaimRepository claimRepository;

    @Override
    @Transactional
    public void createConfirmRequest(ConfirmationRequestDto confirmRequestDto) {
        confirmRequestRepository.save(ConfirmationRequestMapper.mapFromDto(confirmRequestDto));
    }


    @Override
    public LocalDate getConfirmRequestDueDate(ConfirmationRequestDto confirmRequest) {
        LocalDate confirmRequestDate = confirmRequest.getConfirmRequestDate();
        return confirmRequestDate.plusDays(2);
    }

    @Override
    public ConfirmationRequestDto getConfirmRequestById(int confirmRequestId) {

        Optional<ConfirmationRequest> optionalConfirmRequest = confirmRequestRepository.findById(confirmRequestId);

        ConfirmationRequest confirmRequest = null;
        if (optionalConfirmRequest.isPresent()) {
            confirmRequest = optionalConfirmRequest.get();
        } else {
            logger.error("Can't get confirmation request with id = {}", confirmRequestId);
            throw new NoSuchElementException("Not found claim with id = " + confirmRequestId);
        }

        return ConfirmationRequestMapper.mapToDto(confirmRequest);

    }

    @Transactional
    @Override
    public void confirmRequestByClient(int confirmRequestId) throws AppException {

        Optional<ConfirmationRequest> optionalConfirmRequest = confirmRequestRepository.findById(confirmRequestId);
        ConfirmationRequest confirmRequest = null;
        if (optionalConfirmRequest.isPresent()) {
            confirmRequest = optionalConfirmRequest.get();
        } else {
            logger.error("Can't get claim with id = {}", confirmRequestId);
        }
        try {
            ConfirmationRequestDto requestToBeConfirmed = ConfirmationRequestMapper.mapToDto(confirmRequest);
            requestToBeConfirmed.setStatus("confirmed");
            confirmRequestRepository.updateConfirmRequestStatus("confirmed", requestToBeConfirmed.getId());
        } catch (Exception exception) {
            throw new AppException("Can't update confirmation request's status to 'confirmed'", exception);
        }
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
                                confirmRequest.getClaimId().getId(),
                                confirmRequest.getRoomId().getId(),
                                confirmRequest.getConfirmRequestDate(),
                                confirmRequest.getStatus()
                        ));
            }

            return result;
        } catch (Exception exception) {
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

            List<ClaimDto> userClaims = claimRepository.findClaimsByUserId(userID)
                    .stream()
                    .map(ClaimMapper::mapToDto)
                    .collect(Collectors.toList());

            List<ConfirmationRequestClientDto> result = new ArrayList<>();

            for (ConfirmationRequest confirmRequest : allUserConfirmRequests) {
                var claim = userClaims.stream()
                        .filter(a -> a.getId() == confirmRequest.getClaimId().getId())
                        .findFirst()
                        .get();
                result.add(
                        new ConfirmationRequestClientDto(confirmRequest.getId(),
                                confirmRequest.getConfirmRequestDate(),
                                getConfirmRequestDueDate(ConfirmationRequestMapper.mapToDto(confirmRequest)),
                                claim.getRoomSeats(),
                                claim.getRoomClass(),
                                claim.getCheckinDate(),
                                claim.getCheckoutDate(),
                                confirmRequest.getClaimId().getId(),
                                confirmRequest.getStatus()
                        ));
            }
            return result;
        } catch (Exception exception) {
            throw new AppException("Can't retrieve client's confirmation requests to show in the client's account", exception);
        }
    }

}
