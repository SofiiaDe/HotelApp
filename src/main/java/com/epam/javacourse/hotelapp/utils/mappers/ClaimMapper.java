package com.epam.javacourse.hotelapp.utils.mappers;

import com.epam.javacourse.hotelapp.dto.ClaimDto;
import com.epam.javacourse.hotelapp.model.Claim;


public class ClaimMapper {

    public static ClaimDto mapToDto(Claim claim) {
        ClaimDto dto = new ClaimDto();
        dto.setId(claim.getId());
        dto.setUserId(claim.getUserId().getId());
        dto.setCheckinDate(claim.getCheckinDate());
        dto.setCheckoutDate(claim.getCheckoutDate());
        dto.setRoomSeats(claim.getRoomSeats());
        dto.setRoomClass(claim.getRoomClass());
        return dto;
    }

    public static Claim mapFromDto(ClaimDto dto) {
        Claim claim = new Claim();
        claim.setId(dto.getId());
        claim.setUserId(dto.getUser());
        claim.setCheckinDate(dto.getCheckinDate());
        claim.setCheckoutDate(dto.getCheckoutDate());
        claim.setRoomSeats(dto.getRoomSeats());
        claim.setRoomClass(dto.getRoomClass());
        return claim;
    }
}
