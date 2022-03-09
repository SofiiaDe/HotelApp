package com.epam.javacourse.hotelapp.utils.mappers;

import com.epam.javacourse.hotelapp.dto.ConfirmationRequestDto;
import com.epam.javacourse.hotelapp.model.ConfirmationRequest;

public class ConfirmationRequestMapper {

    public static ConfirmationRequestDto mapToDto(ConfirmationRequest confirmRequest){
        ConfirmationRequestDto dto = new ConfirmationRequestDto();
        dto.setId(confirmRequest.getId());
        dto.setUserId(confirmRequest.getUserId().getId());
        dto.setClaimId(confirmRequest.getClaimId().getId());
        dto.setRoomId(confirmRequest.getRoomId().getId());
        dto.setConfirmRequestDate(confirmRequest.getConfirmRequestDate());
        dto.setStatus(confirmRequest.getStatus());
        return dto;
    }


    public static ConfirmationRequest mapFromDto(ConfirmationRequestDto dto){
        ConfirmationRequest confirmRequest = new ConfirmationRequest();
        confirmRequest.setId(dto.getId());
        confirmRequest.setUserId(dto.getUser());
        confirmRequest.setClaimId(dto.getClaim());
        confirmRequest.setRoomId(dto.getRoom());
        confirmRequest.setConfirmRequestDate(dto.getConfirmRequestDate());
        confirmRequest.setStatus(dto.getStatus());
        return confirmRequest;
    }
}
