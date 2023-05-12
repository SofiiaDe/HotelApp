package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.dto.ClaimClientDto;
import com.epam.javacourse.hotelapp.dto.ClaimDto;
import com.epam.javacourse.hotelapp.dto.ClaimManagerDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.NoSuchElementFoundException;
import com.epam.javacourse.hotelapp.model.Claim;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.ClaimRepository;
import com.epam.javacourse.hotelapp.repository.UserRepository;
import com.epam.javacourse.hotelapp.service.interfaces.IClaimService;
import com.epam.javacourse.hotelapp.utils.mappers.ClaimMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaimServiceImpl implements IClaimService {

    private final ClaimRepository claimRepository;
    private final UserRepository userRepository;

    @Autowired
    public ClaimServiceImpl(ClaimRepository claimRepository, UserRepository userRepository) {
        this.claimRepository = claimRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void createClaim(ClaimDto claimDto) {
        claimRepository.save(ClaimMapper.mapFromDto(claimDto));
    }

    @Override
    public ClaimDto getClaimById(int claimId) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new NoSuchElementFoundException("Can't get claim with id = " + claimId));
        return ClaimMapper.mapToDto(claim);
    }


    @Override
    @Transactional(rollbackFor = {Exception.class}, readOnly = true)
    public List<ClaimDto> getClaimsByUserId(int userId) {
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
        } catch (Exception exception) {
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
                var user = users.stream()
                        .filter(u -> u.getId() == claim.getUserId().getId())
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementFoundException("Can't get user with id = " + claim.getUserId().getId()));

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

        } catch (Exception exception) {
            throw new AppException("Can't retrieve all claims to show in the manager's account", exception);
        }
    }

    @Override
    public void removeClaim(int claimId) {
        claimRepository.deleteById(claimId);
    }

    @Override
    public List<ClaimManagerDto> getAllDetailedClaimsPaginated(int page, int pageSize, String sortBy) throws AppException {
        try {

            Pageable paging = PageRequest.of(page - 1, pageSize, Sort.by(sortBy));
            Page<Claim> pagedResult = claimRepository.findAll(paging);

            List<Claim> allClaims;

            if (pagedResult.hasContent()) {
                allClaims = pagedResult.getContent();
            } else {
                allClaims = new ArrayList<>();
            }

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
                var user = users.stream()
                        .filter(u -> u.getId() == claim.getUserId().getId())
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementFoundException(
                                "Can't get user with id = " + claim.getUserId().getId()));

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

        } catch (Exception exception) {
            throw new AppException("Can't retrieve all claims to show in the manager's account", exception);
        }
    }

}
