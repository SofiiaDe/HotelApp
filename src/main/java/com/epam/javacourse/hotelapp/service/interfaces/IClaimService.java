package com.epam.javacourse.hotelapp.service.interfaces;

import com.epam.javacourse.hotelapp.dto.ClaimClientDto;
import com.epam.javacourse.hotelapp.dto.ClaimDto;
import com.epam.javacourse.hotelapp.dto.ClaimManagerDto;
import com.epam.javacourse.hotelapp.exception.AppException;

import java.util.List;

public interface IClaimService {

    List<ClaimDto> getClaimsByUserId(int userId) throws AppException;

    void createClaim(ClaimDto claimDto);

    List<ClaimManagerDto> getAllDetailedClaims() throws AppException;

    List<ClaimManagerDto> getAllDetailedClaimsPaginated(int page, int pageSize, String sortBy) throws AppException;

    void removeClaim(int claimId);

    ClaimDto getClaimById(int claimId) throws AppException;

    List<ClaimClientDto> getUserDetailedClaims(int userID) throws AppException;

}
