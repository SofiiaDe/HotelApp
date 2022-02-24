package com.epam.javacourse.hotelapp.utils.mappers;

import com.epam.javacourse.hotelapp.dto.ConfirmationRequestDto;
import com.epam.javacourse.hotelapp.model.ConfirmationRequest;

public class ConfirmationRequestMapper implements DtoMapper<ConfirmationRequestDto, ConfirmationRequest>{

    @Override
    public ConfirmationRequestDto mapToDto(ConfirmationRequest confirmRequest){
        ConfirmationRequestDto dto = new ConfirmationRequestDto();
        dto.setId(confirmRequest.getId());
        dto.setUserId(confirmRequest.getUserId().getId());
        dto.setApplicationId(confirmRequest.getApplicationId().getId());
        dto.setRoomId(confirmRequest.getRoomId().getId());
        dto.setConfirmRequestDate(confirmRequest.getConfirmRequestDate());
        dto.setStatus(confirmRequest.getStatus());
        dto.setConfirmRequestDueDate(confirmRequest.getConfirmRequestDueDate());
        return dto;
    }

    @Override
    public ConfirmationRequest mapFromDto(ConfirmationRequestDto dto){
        ConfirmationRequest confirmRequest = new ConfirmationRequest();
        confirmRequest.setId(dto.getId());
        confirmRequest.setUserId(dto.getUser());
        confirmRequest.setApplicationId(dto.getApplication());
        confirmRequest.setRoomId(dto.getRoom());
        confirmRequest.setConfirmRequestDate(dto.getConfirmRequestDate());
        confirmRequest.setStatus(dto.getStatus());
        confirmRequest.setConfirmRequestDueDate(dto.getConfirmRequestDueDate());
        return confirmRequest;
    }
}
