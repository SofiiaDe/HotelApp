package com.epam.javacourse.hotelapp.service.interfaces;

import com.epam.javacourse.hotelapp.dto.ClaimClientDto;
import com.epam.javacourse.hotelapp.dto.ClaimDto;
import com.epam.javacourse.hotelapp.dto.ClaimManagerDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.model.Claim;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IClaimService {

    List<ClaimDto> getClaimsByUserId(int userId) throws AppException;

    @Transactional
    void createClaim(ClaimDto claimDto);
//
//    List<ClaimDto> getAllClaims() throws AppException;
//
    List<ClaimManagerDto> getAllDetailedClaims() throws AppException;
//
//
//    boolean updateClaim(Claim claim) throws AppException;
//
//    void removeClaim(int id) throws AppException;

    ClaimDto getClaimById(int claimId) throws AppException;


    List<ClaimClientDto> getUserDetailedClaims(int userID) throws AppException;

}
