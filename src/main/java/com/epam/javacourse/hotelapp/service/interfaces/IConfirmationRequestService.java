package com.epam.javacourse.hotelapp.service.interfaces;

import com.epam.javacourse.hotelapp.dto.ConfirmationRequestClientDto;
import com.epam.javacourse.hotelapp.dto.ConfirmationRequestDto;
import com.epam.javacourse.hotelapp.dto.ConfirmationRequestManagerDto;
import com.epam.javacourse.hotelapp.exception.AppException;

import java.time.LocalDate;
import java.util.List;

public interface IConfirmationRequestService {

    void createConfirmRequest(ConfirmationRequestDto confirmRequestDto);

    ConfirmationRequestDto getConfirmRequestById(int confirmRequestId);

//    List<ConfirmationRequest> getConfirmRequestsByUserId(int userId) throws AppException;
//
//    List<ConfirmationRequest> getAllConfirmRequests() throws AppException;

    List<ConfirmationRequestManagerDto> getAllDetailedConfirmRequests() throws AppException;

//    void deleteConfirmRequestById(int id) throws AppException;

    List<ConfirmationRequestClientDto> getUserDetailedConfirmRequests(int userID) throws AppException;

    /**
     * Calculates due date until which the client has to confirm request
     * @param confirmRequest
     * @return LocalDate
     */
    LocalDate getConfirmRequestDueDate(ConfirmationRequestDto confirmRequest);

    void confirmRequestByClient(int confirmRequestId) throws AppException;
}
