package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.dto.ClaimClientDto;
import com.epam.javacourse.hotelapp.dto.ClaimDto;
import com.epam.javacourse.hotelapp.dto.ClaimManagerDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.Claim;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.*;
import com.epam.javacourse.hotelapp.service.interfaces.IClaimService;
import com.epam.javacourse.hotelapp.utils.mappers.ClaimMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClaimServiceImpl implements IClaimService {

    @Autowired
    ClaimRepository claimRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    RoomRepository roomRepository;

    @Override
    @Transactional
    public void createClaim(ClaimDto claimDto) {
        claimRepository.save(ClaimMapper.mapFromDto(claimDto));
    }


    @Override
    @Transactional(readOnly = true)
    public List<ClaimDto> getClaimsByUserId(int userId) throws AppException {
        final List<ClaimDto> result = new ArrayList<>();
        List<Claim> claims = claimRepository.findClaimsByUserId(userId);

        claims.forEach(x -> result.add(ClaimMapper.mapToDto(x)));
        return result;
    }



    @Override
    public List<ClaimClientDto> getUserDetailedClaims(int userID) throws AppException {

        try {
            List<Claim> allUserClaims = claimRepository.findClaimsByUserId(userID);

            if (allUserClaims.isEmpty()) {
                return Collections.emptyList();
            }

            ArrayList<ClaimClientDto> result = new ArrayList<>();

            for (Claim claim : allUserClaims) {
                result.add(
                         new ClaimClientDto(claim.getId(),
                                claim.getCheckinDate(),
                                claim.getCheckoutDate(),
                                claim.getRoomSeats(),
                                claim.getRoomClass()
                        ));
            }
            return result;
        } catch (DBException exception) {
            throw new AppException("Can't retrieve client's claims to show in the client's account", exception);
        }
    }

    @Override
    public List<ClaimManagerDto> getAllDetailedClaims() throws AppException {
        try {
            List<Claim> allClaims = claimRepository.findAll();

            if (allClaims.isEmpty()) {
                return Collections.emptyList();
            }

            List<Integer> userIds = allClaims.stream()
                    .map(Claim::getUserId)
                    .map(User::getId)
                    .distinct().collect(Collectors.toList());

            List<User> users = userRepository.findUsersByIds(userIds);

            List<ClaimManagerDto> result = new ArrayList<>();

            for (Claim claim : allClaims) {
                Optional<User> optionalUser = users.stream()
                        .filter(u -> u.getId() == claim.getUserId().getId())
                        .findFirst();
                if (optionalUser.isEmpty()) {
                    throw new ChangeSetPersister.NotFoundException();
                }
                User user = optionalUser.get();
                result.add(
                        new ClaimManagerDto(
                                claim.getId(),
                                user.getFirstName() + ' ' + user.getLastName(),
                                user.getEmail(),
                                claim.getCheckinDate(),
                                claim.getCheckoutDate(),
                                claim.getRoomSeats(),
                                claim.getRoomClass()
                        ));
            }
            return result;

        } catch (DBException | ChangeSetPersister.NotFoundException exception) {
            throw new AppException("Can't retrieve all applications to show in the manager's account", exception);
        }

    }
}
